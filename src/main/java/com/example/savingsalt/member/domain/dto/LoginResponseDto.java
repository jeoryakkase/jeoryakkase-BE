package com.example.savingsalt.member.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String nickname;
    private BadgeDto badge;
    private String profileImage;
}
