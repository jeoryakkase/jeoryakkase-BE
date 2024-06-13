package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeType;
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

    private Long id;

    private String challengeTitle;

    private ChallengeType challengeType;

    private String challengeTerm;

    private int challengeCount;

    private int challengeGoal;

    private String authContent;

    private BadgeDto badgeDto;
}
