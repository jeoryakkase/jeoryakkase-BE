package com.example.savingsalt.badge.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BadgeUpdateReqDto {

    @Size(min = 1, max = 20, message = "이름은 1 ~ 20자 이여야 합니다!")
    private String name;

    private String badgeDesc;

    private String badgeType;

    private String stroke;

    private String fill;
}
