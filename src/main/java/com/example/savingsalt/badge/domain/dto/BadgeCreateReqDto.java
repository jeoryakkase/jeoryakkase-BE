package com.example.savingsalt.badge.domain.dto;

import jakarta.validation.constraints.NotBlank;
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
public class BadgeCreateReqDto {

    @NotBlank(message = "뱃지 이름은 필수입력 사항입니다.")
    @Size(min = 1, max = 20, message = "이름은 1 ~ 20자 이여야 합니다!")
    private String name;

    @NotBlank(message = "뱃지 설명은 필수입력 사항입니다.")
    private String badgeDesc;

    @NotBlank(message = "뱃지 사진은 필수입력 사항입니다.")
    @Size(max = 3000, message = "이미지 URL은 최대 3000자까지 저장 가능합니다.")
    private String badgeImage;

    @NotBlank(message = "뱃지 종류는 필수입력 사항입니다.")
    private String badgeType;
}
