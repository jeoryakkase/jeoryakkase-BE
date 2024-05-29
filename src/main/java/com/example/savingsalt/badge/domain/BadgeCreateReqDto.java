package com.example.savingsalt.badge.domain;

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

    private String name;

    private String badgeDesc;

    private String badgeImage;

    private String badgeType;

    public BadgeEntity toEntity(BadgeCreateReqDto badgeCreateReqDto) {
        return BadgeEntity.builder()
            .name(badgeCreateReqDto.getName())
            .badgeDesc(badgeCreateReqDto.getBadgeDesc())
            .badgeImage(badgeCreateReqDto.getBadgeImage())
            .badgeType(badgeCreateReqDto.getBadgeType())
            .build();
    }
}
