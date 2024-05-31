package com.example.savingsalt.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.handler.CustomAuthenticationFailureHandler;
import com.example.savingsalt.handler.CustomAuthenticationSuccessHandler;
import com.example.savingsalt.handler.OAuth2SuccessHandler;
import com.example.savingsalt.member.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final LoginService loginService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
            .requestMatchers(toH2Console())
            .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter(
            authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
            objectMapper, jwtTokenProvider);
        customFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        customFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

        return http
            .authorizeRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/login", "/api/login", "/api/login/oauth2/google", "/signup",
                    "/api/signup", "/api/token")
                .permitAll()
                // swagger 관련 경로 허용
                .requestMatchers("/swagger-ui.html**", "/swagger-ui/**", "/v3/api-docs/**",
                    "/swagger-resources/**", "/webjars/**").permitAll()
                .anyRequest().authenticated())
            // 폼 로그인
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/api/login")
                .defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll())
            // 소셜 로그인
            .oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/login")
                .successHandler(oAuth2SuccessHandler))
            // 로그아웃
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .logoutSuccessHandler(
                    (request, response, authentication) -> { // 성공시 200 반환, 추가적인 리디렉션 x
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Logout success");
                    })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))
            .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(tokenAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}