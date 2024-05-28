package com.example.savingsalt.badge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberGoalBadgeResDto {

    private String name;

    private String badgeDesc;

    private String badgeImage;
}
