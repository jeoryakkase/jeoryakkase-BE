package com.example.savingsalt.config;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.mapper.BadgeMainMapperImpl;
import com.example.savingsalt.badge.service.BadgeServiceImpl;
import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.dto.LoginResponseDto;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    private final BadgeServiceImpl badgeService;
    private final BadgeMainMapperImpl badgeMainMapper;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        JwtTokenProvider jwtTokenProvider, BadgeServiceImpl badgeService,
        BadgeMainMapperImpl badgeMainMapper) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.badgeService = badgeService;
        this.badgeMainMapper = badgeMainMapper;
        setFilterProcessesUrl("/api/login");
    }


    // JSON 요청을 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> credentials = new HashMap<>();
            String contentType = request.getContentType();

            if (contentType != null) {
                if (contentType.equals("application/json")) {
                    credentials = objectMapper.readValue(request.getInputStream(), Map.class);
                } else if (contentType.equals("application/x-www-form-urlencoded")) {
                    credentials.put("username", request.getParameter("username"));
                    credentials.put("password", request.getParameter("password"));
                }
            }

            String username = credentials.get("username");
            if (username == null) {
                username = credentials.get("email");
            }
            String password = credentials.get("password");

            if (username == null || password == null) {
                throw new RuntimeException("Username or password is missing");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);

            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return super.getAuthenticationManager();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain, Authentication authResult) throws IOException, ServletException {
        /// JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authResult);
        String refreshToken = jwtTokenProvider.createRefreshToken(authResult);

        // 응답 헤더에 accessToken 설정
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        MemberEntity memberEntity = (MemberEntity) authResult.getPrincipal();

        Long representativeBadgeId = memberEntity.getRepresentativeBadgeId();
        BadgeDto badgeDto = null;
        if (representativeBadgeId != null) {
            badgeDto = badgeMainMapper.toDto(badgeService.findById(representativeBadgeId));
        }

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
            .nickname(memberEntity.getNickname())
            .profileImage(memberEntity.getProfileImage())
            .badge(badgeDto)
            .build();

        // 응답 바디에 refresh token 및 user 정보 설정
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("refreshToken", refreshToken);
        String loginResponseJson = objectMapper.writeValueAsString(loginResponseDto);
        responseBody.put("user", loginResponseJson);

        // 응답 바디를 JSON 형태로 변환하여 클라이언트에게 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
