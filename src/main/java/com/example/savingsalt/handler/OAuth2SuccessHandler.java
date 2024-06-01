package com.example.savingsalt.handler;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        // 세션에서 newUser 속성 확인
        HttpSession session = request.getSession(false);
        boolean isNewUser = session != null && Boolean.TRUE.equals(session.getAttribute("newUser"));

        if (isNewUser) {
            // 새로운 회원 -> 추가 정보 입력 페이지로 리디렉션
            session.removeAttribute("newUser");  // 속성을 제거하여 상태를 초기화
            getRedirectStrategy().sendRedirect(request, response, "/additional-info");
        } else {
            // 기존 회원 -> 기본 페이지로 리디렉션
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
