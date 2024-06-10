package com.example.savingsalt.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2LoginResponseDto {

    String email;
    String accessToken;
    String refreshToken;
//    String redirectUrl;
}
