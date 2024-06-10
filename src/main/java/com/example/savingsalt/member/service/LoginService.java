package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException.MemberNotFoundException("email", email));
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
            .savePurpose(memberEntity.getSavePurpose())
            .role(memberEntity.getRole())
            .profileImage(memberEntity.getProfileImage())
            .interests(memberEntity.getInterests())
            .about(memberEntity.getAbout())
            .build();
    }
}
