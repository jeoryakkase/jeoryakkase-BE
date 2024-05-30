package com.example.savingsalt.badge.domain.dto;

import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeDto {

    private String name;

    private String badgeImage;

    private String badgeDesc;

    private String badgeType;

    public static BadgeDto fromEntity(BadgeEntity badgeEntity) {
        return BadgeDto.builder()
            .name(badgeEntity.getName())
            .badgeImage(badgeEntity.getBadgeImage())
            .badgeDesc(badgeEntity.getBadgeDesc())
            .badgeType(badgeEntity.getBadgeType())
            .build();
    }
}
