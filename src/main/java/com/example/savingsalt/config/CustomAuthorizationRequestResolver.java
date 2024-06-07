package com.example.savingsalt.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;
    private final Map<String, String> authorizationRequestBaseUris = new HashMap<>();

    public CustomAuthorizationRequestResolver(
        ClientRegistrationRepository clientRegistrationRepository,
        String authorizationRequestBaseUri) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, authorizationRequestBaseUri);
        authorizationRequestBaseUris.put("google", "/api/login/oauth2/google");
        authorizationRequestBaseUris.put("kakao", "/api/login/oauth2/kakao");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        String registrationId = this.getRegistrationId(request);
        if (registrationId != null) {
            request.setAttribute("registration_id", registrationId);
        }
        return defaultResolver.resolve(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request,
        String clientRegistrationId) {
        return defaultResolver.resolve(request, clientRegistrationId);
    }

    private String getRegistrationId(HttpServletRequest request) {
        return authorizationRequestBaseUris.entrySet().stream()
            .filter(entry -> request.getRequestURI().startsWith(entry.getValue()))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
    }
}
