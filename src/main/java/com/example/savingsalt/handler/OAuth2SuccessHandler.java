package com.example.savingsalt.handler;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.OAuth2LoginResponseDto;
import com.example.savingsalt.member.domain.RefreshToken;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.savingsalt.member.repository.RefreshTokenRepository;
import com.example.savingsalt.member.service.MemberService;
import com.example.savingsalt.util.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//    private String REDIRECT_PATH = "/";

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));

//        // 클라이언트에서 토큰 받아오기
//        String accessToken = request.getParameter("access_token");
//        String refreshToken = request.getHeader("refresh_token");
//
//        if(!jwtTokenProvider.validateToken(accessToken)) {
//            throw new InvalidTokenException();
//        }
//
//        // 토큰에서 사용자 정보 추출
//        Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
//        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail((String) oAuth2User.getAttributes().get("email")));
//
//        // 리프레시 토큰 저장
//        saveRefreshToken(memberEntity.getId(), refreshToken);
//        addRefreshTokenToCookie(request, response, refreshToken);
//
//        response.setHeader("Authorization", "Bearer " + accessToken);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter writer = response.getWriter();
//        writer.write("{\"refreshToken\": \"" + refreshToken + "\"}");
//        writer.flush();

        // JWT 토큰 생성
        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);
        saveRefreshToken(memberEntity.getId(), tokenResponseDto.getRefreshToken());
        addRefreshTokenToCookie(request, response, tokenResponseDto.getRefreshToken());

        // 새로운 회원 여부를 쿠키에서 확인
//        Cookie newUserCookie = WebUtils.getCookie(request, "newUser");
//        boolean isNewUser = newUserCookie != null && "true".equals(newUserCookie.getValue());
//
//        if (isNewUser) {
//            // 새로운 회원 -> 추가 정보 입력 페이지로 리디렉션
//            CookieUtil.deleteCookie(request, response, "newUser"); // 쿠키를 제거하여 상태를 초기화
//            REDIRECT_PATH = "/userinfo/edit";
//        }

        // 응답 바디에 이메일, 토큰, 리디렉션 url 담아 전송
        OAuth2LoginResponseDto loginResponseDto = OAuth2LoginResponseDto.builder()
            .email(email)
            .accessToken(tokenResponseDto.getAccessToken())
            .refreshToken(tokenResponseDto.getRefreshToken())
//            .redirectUrl(REDIRECT_PATH)
            .build();

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponseDto));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 리프레시 토큰을 데이터베이스에 저장
    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
            .map(entity -> entity.update(newRefreshToken))
            .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
        String refreshToken) {
        int cookieMaxAge = (int) Duration.ofDays(14).toSeconds();
        CookieUtil.deleteCookie(request, response, "refresh_token");
        CookieUtil.addCookie(response, "refresh_token", refreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request,
        HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
