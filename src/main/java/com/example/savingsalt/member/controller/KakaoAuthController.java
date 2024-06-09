//package com.example.savingsalt.member.controller;
//
//import com.example.savingsalt.config.jwt.JwtTokenProvider;
//import com.example.savingsalt.member.domain.TokenResponseDto;
//import com.example.savingsalt.member.service.OAuth2UserCustomService;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api")
//@Tag(name = "Kakao OAuth 2.0 Authentication")
//public class KakaoAuthController {
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
//    private String clientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String redirectUri;
//
//    private final OAuth2UserCustomService oAuth2UserCustomService;
//    private final ClientRegistrationRepository clientRegistrationRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Operation(summary = "Authenticate with Kakao OAuth 2.0", description = "Exchanges the authorization code for an access token and retrieves the user information from Kakao.")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
//        @ApiResponse(responseCode = "400", description = "Invalid request or Kakao token exchange failed"),
//        @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @GetMapping("/kakao-authcode")
//    public ResponseEntity<?> kakaoAuthCode(@RequestParam String code) {
//        try {
//            // Kakao OAuth2 token exchange
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("grant_type", "authorization_code");
//            params.add("client_id", clientId);
//            params.add("client_secret", clientSecret);
//            params.add("redirect_uri", redirectUri);
//            params.add("code", code);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//            ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
//                "https://kauth.kakao.com/oauth/token",
//                request,
//                KakaoTokenResponse.class
//            );
//
//            KakaoTokenResponse tokenResponse = response.getBody();
//            String accessToken = tokenResponse.getAccessToken();
//
//            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(
//                "kakao");
//            OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
//                OAuth2AccessToken.TokenType.BEARER,
//                accessToken,
//                Instant.now(),
//                null
//            );
//
//            OAuth2UserRequest userRequest = new OAuth2UserRequest(
//                clientRegistration,
//                oAuth2AccessToken
//            );
//
//            OAuth2User user = oAuth2UserCustomService.loadUser(userRequest);
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
//                user.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);
//
//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.set(HttpHeaders.AUTHORIZATION,
//                "Bearer " + tokenResponseDto.getAccessToken());
//
//            Map<String, String> responseBody = new HashMap<>();
//            responseBody.put("refreshToken", tokenResponseDto.getRefreshToken());
//
//            return ResponseEntity.status(HttpStatus.OK)
//                .headers(responseHeaders)
//                .body(responseBody);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Kakao authentication failed");
//        }
//    }
//
//    @Getter
//    static class KakaoTokenResponse {
//
//        @JsonProperty("access_token")
//        private String accessToken;
//        @JsonProperty("token_type")
//        private String tokenType;
//        @JsonProperty("refresh_token")
//        private String refreshToken;
//        @JsonProperty("expires_in")
//        private Long expiresIn;
//        @JsonProperty("scope")
//        private String scope;
//    }
//}
//
