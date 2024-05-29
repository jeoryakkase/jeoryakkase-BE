package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalArgumentException(("User not found with email: " + email)));
        log.info("User found with email: {}", email);
        return memberEntity;
    }

    private UserDetails createUserDetails(MemberEntity memberEntity) {
        return MemberEntity.builder()
            .email(memberEntity.getEmail())
            .password(memberEntity.getPassword())
            .nickname(memberEntity.getNickname())
            .age(memberEntity.getAge())
            .gender(memberEntity.getGender())
            .income(memberEntity.getIncome())
            .savingGoal(memberEntity.getSavingGoal())
            .role(memberEntity.getRole())
            .profileImage(memberEntity.getProfileImage())
            .build();
    }
}
