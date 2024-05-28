package com.example.savingsalt.challenge.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberChallengeCreateReqDto {

    private String challengeTitle;

    private String challengeDesc;

    private int challengeGoal;

    private int challengeCount;

    private String challengeType;

    private String challengeTerm;

    private ChallengeEntity.ChallengeDifficulty challengeDifficulty;
}
