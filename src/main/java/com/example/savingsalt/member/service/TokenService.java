package com.example.savingsalt.member.service;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.TokenResponseDto;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenResponseDto createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        MemberEntity memberEntity = memberService.findMemberById(memberId).toEntity();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberEntity.getEmail(), memberEntity.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateToken(authentication);
    }
}
