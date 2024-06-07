package com.example.savingsalt.config;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        JwtTokenProvider jwtTokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
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
        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authResult);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponseDto));
    }
}
