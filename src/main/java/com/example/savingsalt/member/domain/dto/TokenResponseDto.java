package com.example.savingsalt.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponseDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
