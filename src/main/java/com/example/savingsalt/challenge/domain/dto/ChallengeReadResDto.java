package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeReadResDto {

    private String challengeTitle;

    private String challengeType;

    private String challengeTerm;

    private int challengeCount;

    private BadgeDto badgeDto;
}
