package com.example.savingsalt.handler;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.RefreshToken;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.repository.RefreshTokenRepository;
import com.example.savingsalt.util.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);

        MemberEntity memberEntity = (MemberEntity) authentication.getPrincipal();
        saveRefreshToken(memberEntity.getId(), tokenResponseDto.getRefreshToken());
        addRefreshTokenToCookie(request, response, tokenResponseDto.getRefreshToken());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponseDto));
    }

    // 리프레시 토큰을 데이터베이스에 저장
    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
            .map(entity -> entity.update(newRefreshToken))
            .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) Duration.ofDays(14).toSeconds();
        CookieUtil.deleteCookie(request, response, "refresh_token");
        CookieUtil.addCookie(response, "refresh_token", refreshToken, cookieMaxAge);
    }
}
