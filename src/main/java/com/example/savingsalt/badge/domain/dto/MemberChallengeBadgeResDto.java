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
public class MemberChallengeBadgeResDto {

    private String name;

    private String badgeDesc;

    private String badgeImage;

    public static MemberChallengeBadgeResDto fromEntity(BadgeEntity badgeEntity) {
        return MemberChallengeBadgeResDto.builder()
            .name(badgeEntity.getName())
            .badgeDesc(badgeEntity.getBadgeDesc())
            .badgeImage(badgeEntity.getBadgeImage())
            .build();
    }
}
