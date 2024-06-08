package com.example.savingsalt.member.service;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.LoginRequestDto;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.MemberDto;
import com.example.savingsalt.member.domain.OAuth2SignupRequestDto;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.enums.Gender;
import com.example.savingsalt.member.enums.Role;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.exception.MemberException.InvalidPasswordException;
import com.example.savingsalt.member.exception.MemberException.InvalidTokenException;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.repository.MemberRepository;
import com.example.savingsalt.member.repository.RefreshTokenRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;
    private final MemberMapper memberMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    @Transactional
    public MemberEntity signUp(SignupRequestDto dto) {
        checkEmail(dto.getEmail()); // 이메일 중복 검사
        checkNickname(dto.getNickname()); // 닉네임 중복 검사

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail(dto.getEmail());
        memberEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        memberEntity.setNickname(dto.getNickname());
        memberEntity.setAge(dto.getAge());
        memberEntity.setGender(Gender.valueOf(dto.getGender()));
        memberEntity.setIncome(dto.getIncome());
        memberEntity.setSavePurpose(dto.getSavePurpose());
        memberEntity.setProfileImage(dto.getProfileImage());
        memberEntity.setInterests(dto.getInterests());
        memberEntity.setAbout(dto.getAbout());
        memberEntity.setRole(Role.MEMBER);

        return memberRepository.save(memberEntity);
    }

    // OAuth2 회원가입 (추가 정보 입력)
//    public MemberEntity saveAdditionalInfo(OAuth2SignupRequestDto dto) {
//        String email = dto.getEmail();
//        MemberEntity memberEntity = memberRepository.findByEmail(email)
//            .orElseThrow(() -> new MemberException.MemberNotFoundException("email", email));
//
//        checkNickname(dto.getNickname()); // 닉네임 중복 검사
//
//        memberEntity.setNickname(dto.getNickname());
//        memberEntity.setAge(dto.getAge());
//        memberEntity.setGender(Gender.valueOf(dto.getGender()));
//        memberEntity.setIncome(dto.getIncome());
//        memberEntity.setSavePurpose(dto.getSavePurpose());
//        memberEntity.setProfileImage(dto.getProfileImage());
//        memberEntity.setInterests(dto.getInterests());
//        memberEntity.authorizeUser();
//
//        return memberRepository.save(memberEntity);
//    }

    // 로그인
    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto dto) {
        // 클라이언트로부터 토큰 받아오기
        String clientAccessToken = dto.getAccessToken();
        String clientRefreshToken = dto.getRefreshToken();

        // 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(clientAccessToken)) {
            throw new InvalidTokenException();
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(clientAccessToken);

        // 토큰에서 사용자 정보 추출
        MemberEntity memberEntity = (MemberEntity) loginService.loadUserByUsername(
            authentication.getName());
        if (memberEntity == null) {
            throw new MemberNotFoundException("email", memberEntity.getEmail());
        }

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
            .accessToken(clientAccessToken)
            .refreshToken(clientRefreshToken)
            .build();

        return tokenResponseDto;

//        if (!bCryptPasswordEncoder.matches(dto.getPassword(),
//            memberEntity.getPassword())) { // 비밀번호 확인
//            throw new InvalidPasswordException();
//        }
//
        // 인증 토큰 생성
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//            dto.getEmail(), dto.getPassword());
//
//        // 인증 수행
//        Authentication authentication = authenticationManagerBuilder.getObject()
//            .authenticate(authenticationToken);
//
//        // JWT 토큰 생성
//        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);
//
//        return tokenResponseDto;
    }

    // 회원 정보 수정
    @Transactional
    public MemberEntity updateMember(Long id, String email, String password, String nickname,
        int age, String gender,
        int income, String savePurpose, String profileImage, List<Long> interests, String about) {
        MemberEntity memberEntity = memberRepository.findById(id)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("id", id));

        if (!memberEntity.getEmail().equals(email)) { // 이메일을 수정하는 경우
            checkEmail(email); // 이메일 중복 검사
        }

        if (!memberEntity.getNickname().equals(nickname)) { // 닉네임을 수정하는 경우
            checkNickname(nickname); // 닉네임 중복 검사
        }

        if (password != null && !password.isEmpty()) { // 수정 폼에서 비밀번호 필드는 비어있고, 수정할 경우에만 입력
            memberEntity.setPassword(bCryptPasswordEncoder.encode(password));
        }

        memberEntity.setNickname(nickname);
        memberEntity.setAge(age);
        memberEntity.setGender(Gender.valueOf(gender));
        memberEntity.setIncome(income);
        memberEntity.setSavePurpose(savePurpose);
        memberEntity.setProfileImage(profileImage);
        memberEntity.setInterests(interests);
        memberEntity.setAbout(about);

        return memberRepository.save(memberEntity);
    }

    // 이메일 중복 검사
    public boolean checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException.EmailAlreadyExistsException();
        }
        return true;
    }

    // 닉네임 중복 검사
    public boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberException.NicknameAlreadyExistsException();
        }
        return true;
    }

    // 회원 탈퇴
    @Transactional
    public void signOut(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("Id", memberId));

        // 사용자 엔티티 삭제
        memberRepository.delete(memberEntity);

    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("id", memberId));

        // 리프레시 토큰 삭제
        refreshTokenRepository.deleteByMemberId(memberId);

        // 회원 삭제
        memberRepository.delete(memberEntity);
    }

    // 모든 회원 찾기
    public List<MemberDto> findAllMembers() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDto> memberDtos = memberEntities.stream().map(memberMapper::toDto).collect(
            Collectors.toList());

        return memberDtos;
    }

    // id로 회원 찾기
    public MemberDto findMemberById(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("id", id));
        return memberMapper.toDto(memberEntity);
    }

    // 이메일로 회원 찾기
    public MemberDto findMemberByEmail(String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new MemberException.MemberNotFoundException("email", email));
        return memberMapper.toDto(memberEntity);
    }

    // 닉네임으로 회원 찾기
    public MemberEntity findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
            .orElseThrow(
                () -> new MemberException.MemberNotFoundException("nickname", nickname));
    }

    // 같은 수입 범위 내에 있는 회원들 찾기
    public List<MemberEntity> findMembersByIncome(int income) {
        int startIncome = 0;
        int endIncome = 0;

        if (income <= 100) {
            startIncome = 0;
            endIncome = 100;
        } else if (income <= 200) {
            startIncome = 101;
            endIncome = 200;
        } else if (income <= 300) {
            startIncome = 201;
            endIncome = 300;
        } else if (income <= 400) {
            startIncome = 301;
            endIncome = 400;
        } else if (income <= 500) {
            startIncome = 401;
            endIncome = 500;
        } else if (income <= 600) {
            startIncome = 501;
            endIncome = 600;
        } else {
            startIncome = 601;
            endIncome = 10000;
        }

        return memberRepository.findAllByIncomeBetween(startIncome, endIncome);
    }

    // 같은 성별인 회원들 찾기
    public List<MemberEntity> findMembersByGender(Gender gender) {
        return memberRepository.findAllByGender(gender);
    }

    // 같은 나이대인 회원들 찾기
    public List<MemberEntity> findMembersByAge(int age) {
        int startAge = age / 10 * 10;
        int endAge = startAge + 9;

        return memberRepository.findAllByAgeBetween(startAge, endAge);
    }
}
