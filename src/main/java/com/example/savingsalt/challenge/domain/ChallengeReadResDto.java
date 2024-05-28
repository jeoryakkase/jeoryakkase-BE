package com.example.savingsalt.challenge.domain;

import com.example.savingsalt.badge.domain.BadgeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeReadResDto {

    private String challengeTitle;

    private String challengeType;

    private String challengeTerm;

    private int challengeCount;

    private BadgeDto badgeDto;
}
