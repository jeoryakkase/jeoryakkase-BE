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

    @Size(max = 3000, message = "이미지 URL은 최대 3000자까지 저장 가능합니다.")
    private String badgeImage;

    private String badgeType;
}
