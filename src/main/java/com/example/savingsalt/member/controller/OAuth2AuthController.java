package com.example.savingsalt.member.controller;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.dto.LoginResponseDto;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.domain.dto.TokenResponseDto;
import com.example.savingsalt.member.domain.entity.TokenEntity;
import com.example.savingsalt.member.repository.TokenRepository;
import com.example.savingsalt.member.service.OAuth2UserCustomService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "OAuth 2.0 Authentication", description = "OAuth2 API")
public class OAuth2AuthController {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    private static final int ACCESS_TOKEN_EXPIRE_TIME = 2 * 60 * 60 * 1000; // 2시간

    @Operation(summary = "카카오 토큰 검증", description = "Validates the token provided by Kakao and authenticates the user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or Kakao token exchange failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/kakao-auth")
    public ResponseEntity<?> kakaoAuthToken(@RequestHeader("Authorization") String authorizationHeader) {
        return authenticateWithToken(authorizationHeader, "kakao");
    }

    @Operation(summary = "구글 토큰 검증", description = "Validates the token provided by Google and authenticates the user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or Google token exchange failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/google-authcode")
    public ResponseEntity<?> googleAuthToken(@RequestHeader("Authorization") String authorizationHeader) {
        return authenticateWithToken(authorizationHeader, "google");
    }

    private ResponseEntity<?> authenticateWithToken(String authorizationHeader, String registrationId) {
        try {
            String accessToken = authorizationHeader.replace("Bearer ", "");
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

            OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                accessToken,
                Instant.now(),
                null
            );

            OAuth2UserRequest userRequest = new OAuth2UserRequest(
                clientRegistration,
                oAuth2AccessToken
            );

            OAuth2User user = oAuth2UserCustomService.loadUser(userRequest);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // DB에 저장
            saveTokenToDatabase(user, accessToken);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
    }

    private void saveTokenToDatabase(OAuth2User user, String accessToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(user.getName());
        tokenEntity.setAccessToken(accessToken);
        tokenEntity.setExpiresAt(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRE_TIME));

        tokenRepository.save(tokenEntity);
    }
}
