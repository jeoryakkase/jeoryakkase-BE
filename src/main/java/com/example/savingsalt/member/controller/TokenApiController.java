package com.example.savingsalt.member.controller;

import com.example.savingsalt.member.domain.AccessTokenRequestDto;
import com.example.savingsalt.member.domain.AccessTokenResponseDto;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenResponseDto> createNewAccessToken(
        @RequestBody AccessTokenRequestDto request) {
        TokenResponseDto tokenResponseDto = tokenService.createNewAccessToken(request.getRefreshToken());

        AccessTokenResponseDto accessTokenResponseDto = new AccessTokenResponseDto(tokenResponseDto.getAccessToken());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(accessTokenResponseDto);
    }
}
