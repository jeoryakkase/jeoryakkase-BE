package com.example.savingsalt.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.handler.CustomAuthenticationFailureHandler;
import com.example.savingsalt.handler.CustomAuthenticationSuccessHandler;
import com.example.savingsalt.handler.OAuth2SuccessHandler;
import com.example.savingsalt.member.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Lazy // 순환 참조 방지
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
            .requestMatchers(toH2Console())
            .requestMatchers("/static/**");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 메서드 허용
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter(
            authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
            objectMapper, jwtTokenProvider);
        customFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        customFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

        return http
            .cors(withDefaults())
            .authorizeRequests(authorizeRequests -> authorizeRequests
//                .requestMatchers("/", "/login", "/api/login", "/api/login/oauth2/google", "/api/login/oauth2/kakao", "/signup",
//                    "/api/signup", "/api/token")
//                .permitAll()
//                // swagger 관련 경로 허용
//                .requestMatchers("/swagger-ui.html**", "/swagger-ui/**", "/v3/api-docs/**",
//                    "/swagger-resources/**", "/webjars/**").permitAll()
//                .anyRequest().authenticated())
                .anyRequest().permitAll()) // 테스트용
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
                .authorizationEndpoint(authorizationEndpoint ->
                    authorizationEndpoint
                        .authorizationRequestResolver(customAuthorizationRequestResolver())
                        .authorizationRequestRepository(
                            oAuth2AuthorizationRequestBasedOnCookieRepository()))
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
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
        return new CustomAuthorizationRequestResolver(clientRegistrationRepository,
            "/api/login/oauth2/authorization");
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
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
