package com.example.savingsalt.challenge.domain;

import com.example.savingsalt.badge.domain.BadgeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeDetailsReadResDto {

    private String challengeTitle;

    private String challengeDesc;

    private int challengeGoal;

    private int challengeCount;

    private String challengeType;

    private String challengeTerm;

    private ChallengeEntity.ChallengeDifficulty challengeDifficulty;

    private BadgeDto badgeDto;
}
