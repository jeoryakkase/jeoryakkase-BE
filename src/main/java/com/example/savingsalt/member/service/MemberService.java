package com.example.savingsalt.member.service;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.LoginRequestDto;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.MemberDto;
import com.example.savingsalt.member.domain.OAuth2SignupRequestDto;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.enums.Role;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.exception.MemberException.InvalidPasswordException;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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

    // 회원가입
    @Transactional
    public MemberEntity signUp(SignupRequestDto dto) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new MemberException.EmailAlreadyExistsException();
        }

        // 닉네임 중복 검사
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new MemberException.NicknameAlreadyExistsException();
        }

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail(dto.getEmail());
        memberEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        memberEntity.setNickname(dto.getNickname());
        memberEntity.setAge(dto.getAge());
        memberEntity.setGender(dto.getGender());
        memberEntity.setIncome(dto.getIncome());
        memberEntity.setSavingGoal(dto.getSavingGoal());
        memberEntity.setProfileImage(dto.getProfileImage());
        memberEntity.setRole(Role.MEMBER);

        return memberRepository.save(memberEntity);
    }

    // OAuth2 회원가입 (추가 정보 입력)
    public MemberEntity saveAdditionalInfo(OAuth2SignupRequestDto dto) {
        String email = dto.getEmail();
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("email", email));

        // 닉네임 중복 검사
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new MemberException.NicknameAlreadyExistsException();
        }

        memberEntity.setNickname(dto.getNickname());
        memberEntity.setAge(dto.getAge());
        memberEntity.setGender(dto.getGender());
        memberEntity.setIncome(dto.getIncome());
        memberEntity.setSavingGoal(dto.getSavingGoal());
        memberEntity.setProfileImage(dto.getProfileImage());
        memberEntity.authorizeUser();

        return memberRepository.save(memberEntity);
    }

    // 로그인
    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto dto) {
        MemberEntity memberEntity = (MemberEntity) loginService.loadUserByUsername(
            dto.getEmail()); // 회원이 존재하는지 확인
        if (!bCryptPasswordEncoder.matches(dto.getPassword(),
            memberEntity.getPassword())) { // 비밀번호 확인
            throw new InvalidPasswordException();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            dto.getEmail(), dto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);

        return tokenResponseDto;
    }

    // 회원 정보 수정
    @Transactional
    public MemberEntity updateMember(Long id, String password, String nickname, int age, int gender,
        int income, int savingGoal, String profileImage) {
        MemberEntity memberEntity = memberRepository.findById(id)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("id", id));

        if (!memberEntity.getNickname().equals(nickname) && memberRepository.existsByNickname(
            nickname)) {
            throw new MemberException.NicknameAlreadyExistsException();
        }

        if (password != null && !password.isEmpty()) { // 수정 폼에서 비밀번호 필드는 비어있고, 수정할 경우에만 입력
            memberEntity.setPassword(bCryptPasswordEncoder.encode(password));
        }

        memberEntity.setNickname(nickname);
        memberEntity.setAge(age);
        memberEntity.setGender(gender);
        memberEntity.setIncome(income);
        memberEntity.setSavingGoal(savingGoal);
        memberEntity.setProfileImage(profileImage);

        return memberRepository.save(memberEntity);
    }

    // 모든 회원 찾기
    public List<MemberDto> findAllMembers() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntities) {
            memberDtos.add(memberMapper.toDto(memberEntity));
        }

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
    public List<MemberEntity> findMembersByGender(int gender) {
        return memberRepository.findAllByGender(gender);
    }

    // 같은 나이대인 회원들 찾기
    public List<MemberEntity> findMembersByAge(int age) {
        int startAge = age / 10 * 10;
        int endAge = startAge + 9;

        return memberRepository.findAllByAgeBetween(startAge, endAge);
    }
}
