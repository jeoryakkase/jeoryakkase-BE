//package com.example.savingsalt.member.controller;
//
//import com.example.savingsalt.config.jwt.JwtTokenProvider;
//import com.example.savingsalt.member.domain.dto.TokenResponseDto;
//import com.example.savingsalt.member.service.OAuth2UserCustomService;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api")
//@Tag(name = "Google OAuth 2.0 Authentication")
//public class GoogleAuthController {
//
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//    private String clientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    private String redirectUri;
//
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    private final OAuth2UserCustomService oAuth2UserCustomService;
//    private final ClientRegistrationRepository clientRegistrationRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Operation(summary = "Authenticate with Google OAuth 2.0", description = "Exchanges the authorization code for an access token and retrieves the user information from Google.")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
//        @ApiResponse(responseCode = "400", description = "Invalid request or Google token exchange failed"),
//        @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @GetMapping("/google-authcode")
//    public ResponseEntity<?> googleAuthCode(@RequestParam String code) {
//        try {
//            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JSON_FACTORY,
//                "https://oauth2.googleapis.com/token",
//                clientId,
//                clientSecret,
//                code,
//                redirectUri)
//                .execute();
//
//            String accessToken = tokenResponse.getAccessToken();
//
//            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(
//                "google");
//            OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
//                TokenType.BEARER,
//                accessToken,
//                new Date().toInstant(),
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
//            HttpHeaders headers = new HttpHeaders();
//            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponseDto.getAccessToken());
//
//            Map<String, String> responseBody = new HashMap<>();
//            responseBody.put("refreshToken", tokenResponseDto.getRefreshToken());
//
//            return ResponseEntity.status(HttpStatus.OK)
//                .headers(headers)
//                .body(responseBody);
//        } catch (GeneralSecurityException | IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Google authentication failed");
//        }
//    }
//}
