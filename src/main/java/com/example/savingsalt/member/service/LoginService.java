package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.Member;
import com.example.savingsalt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Override
    public Member loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalArgumentException(("User not found with email: " + email)));
        logger.info("User found with email: {}", email);
        return member;
    }
}
