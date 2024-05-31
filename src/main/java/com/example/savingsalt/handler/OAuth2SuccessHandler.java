package com.example.savingsalt.handler;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        MemberEntity memberEntity = memberRepository.findByEmail(email).orElse(null);
        if (memberEntity == null) {
            // 새로운 회원 -> 추가 정보 입력 페이지로 리디렉션
            getRedirectStrategy().sendRedirect(request, response, "/additional-info");
        } else {
            // 기존 회원 -> 기본 페이지로 리디렉션
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
