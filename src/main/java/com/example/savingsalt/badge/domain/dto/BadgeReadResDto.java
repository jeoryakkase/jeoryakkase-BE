package com.example.savingsalt.badge.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BadgeReadResDto {

    private String name;

    private String badgeDesc;

    private String badgeImage;

    private String badgeType;
}
