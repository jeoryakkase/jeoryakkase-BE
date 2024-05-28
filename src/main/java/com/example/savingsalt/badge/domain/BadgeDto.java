package com.example.savingsalt.badge.domain;

import lombok.AccessLevel;
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
}
