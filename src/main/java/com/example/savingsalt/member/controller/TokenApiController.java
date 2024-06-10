package com.example.savingsalt.member.controller;

import com.example.savingsalt.member.domain.dto.AccessTokenRequestDto;
import com.example.savingsalt.member.domain.dto.AccessTokenResponseDto;
import com.example.savingsalt.member.domain.dto.TokenResponseDto;
import com.example.savingsalt.member.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Token", description = "토큰 API")
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    @Operation(summary = "새 액세스 토큰 발급하기", description = "Generate a new access token using a refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Token created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<AccessTokenResponseDto> createNewAccessToken(
        @RequestBody AccessTokenRequestDto request) {
        TokenResponseDto tokenResponseDto = tokenService.createNewAccessToken(request.getRefreshToken());

        AccessTokenResponseDto accessTokenResponseDto = new AccessTokenResponseDto(tokenResponseDto.getAccessToken());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(accessTokenResponseDto);
    }
}
