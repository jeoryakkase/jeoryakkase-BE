package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeDifficulty;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeType;
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

    private ChallengeType challengeType;

    private String challengeTerm;

    private ChallengeDifficulty challengeDifficulty;

    private String authContent;

    private BadgeDto badgeDto;
}
