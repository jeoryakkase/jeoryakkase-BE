package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.Member;
import com.example.savingsalt.member.domain.MemberDto;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.enums.Role;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public Member signUp(SignupRequestDto dto) throws Exception {
        if (memberRepository.existsMemberByEmail(dto.getEmail())) {
            throw new Exception("이미 회원가입 한 이메일입니다.");
        }

        if (memberRepository.existsMemberByNickname(dto.getNickname())) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = memberRepository.save(Member.builder()
            .email(dto.getEmail())
            .password(bCryptPasswordEncoder.encode(dto.getPassword()))
            .nickname(dto.getNickname())
            .age(dto.getAge())
            .gender(dto.getGender())
            .income(dto.getIncome())
            .savingGoal(dto.getSavingGoal())
            .profileImage(dto.getProfileImage())
            .role(Role.MEMBER)
            .build());

        return member;
    }

    // 회원 정보 수정
    public Member updateMember(Long id, String password, String nickname, int age, int gender,
        int income, int savingGoal, String profileImage) throws Exception {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));

        if (memberRepository.existsMemberByNickname(nickname)) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        member.setPassword(bCryptPasswordEncoder.encode(password));
        member.setNickname(nickname);
        member.setAge(age);
        member.setGender(gender);
        member.setIncome(income);
        member.setSavingGoal(savingGoal);
        member.setProfileImage(profileImage);

        return memberRepository.save(member);
    }

    // 모든 회원 찾기
    public List<MemberDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(member.toDto());
        }

        return memberDtos;
    }

    // id로 회원 찾기
    public MemberDto findMemberById(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
        return member.toDto();
    }

    // 이메일로 회원 찾기
    public MemberDto findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalArgumentException("Member not found with email: " + email));
        return member.toDto();
    }

    // 닉네임으로 회원 찾기
    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
            .orElseThrow(
                () -> new IllegalArgumentException("Member not found with nickname: " + nickname));
    }

    // 같은 수입 범위 내에 있는 회원들 찾기
    public List<Member> findMembersByIncome(int income) {
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

        return memberRepository.findByIncomeBetween(startIncome, endIncome);
    }

    // 같은 성별인 회원들 찾기
    public List<Member> findMembersByGender(int gender) {
        return memberRepository.findByGender(gender);
    }

    // 같은 나이대인 회원들 찾기
    public List<Member> findMembersByAge(int age) {
        int startAge = age / 10 * 10;
        int endAge = startAge + 9;

        return memberRepository.findByAgeBetween(startAge, endAge);
    }
}
