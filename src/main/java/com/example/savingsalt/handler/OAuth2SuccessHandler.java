package com.example.savingsalt.handler;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import com.example.savingsalt.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        // 새로운 회원 여부를 쿠키에서 확인
        Cookie newUserCookie = WebUtils.getCookie(request, "newUser");
        boolean isNewUser = newUserCookie != null && "true".equals(newUserCookie.getValue());

        if (isNewUser) {
            // 새로운 회원 -> 추가 정보 입력 페이지로 리디렉션
            CookieUtil.deleteCookie(request, response, "newUser"); // 쿠키를 제거하여 상태를 초기화
            getRedirectStrategy().sendRedirect(request, response, "/additional-info");
        } else {
            // 기존 회원 -> 기본 페이지로 리디렉션
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
