package com.example.savingsalt.challenge.domain.dto;


import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeDifficulty;
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

    private ChallengeDifficulty challengeDifficulty;
}
