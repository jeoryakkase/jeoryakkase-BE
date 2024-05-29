package com.example.savingsalt.challenge.domain;

import com.example.savingsalt.badge.domain.BadgeDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChallengeDto {

    private String challengeTitle;

    private String challengeDesc;

    private int challengeGoal;

    private int challengeCount;

    private String challengeType;

    private String challengeTerm;

    private ChallengeEntity.ChallengeDifficulty challengeDifficulty;

    private String authContent;

    private BadgeDto badgeDto;
}
