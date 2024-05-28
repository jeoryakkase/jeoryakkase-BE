package com.example.savingsalt.badge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadgeUpdateReqDto {

    private String name;

    private String badgeDesc;

    private String badgeImage;

    private String badgeType;
}
