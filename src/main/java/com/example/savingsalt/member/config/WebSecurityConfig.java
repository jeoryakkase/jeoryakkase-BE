package com.example.savingsalt.member.config;

import com.example.savingsalt.member.handler.CustomAuthenticationFailureHandler;
import com.example.savingsalt.member.handler.CustomAuthenticationSuccessHandler;
import com.example.savingsalt.member.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final LoginService loginService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter(
            authenticationManager, objectMapper);
        customFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        customFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

        return http.authorizeRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/login", "/api/login", "/signup", "/api/signup").permitAll()
                .anyRequest().authenticated())
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/api/login")
                .defaultSuccessUrl("/")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/login")
                .logoutSuccessHandler(
                    (request, response, authentication) -> { // 성공시 200 반환, 추가적인 리디렉션 x
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))
            .csrf(csrf -> csrf.disable()) // csrf 비활성화
            .addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        LoginService userDetailService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(loginService)
            .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
