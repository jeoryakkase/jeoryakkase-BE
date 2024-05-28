package com.example.savingsalt.challenge.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeUpdateReqDto {

    private String challengeTitle;

    private String challengeDesc;

    private int challengeGoal;

    private int challengeCount;

    private String challengeType;

    private String challengeTerm;

    private ChallengeEntity.ChallengeDifficulty challengeDifficulty;

    private LocalDateTime updateAt;

    private String authContent;
}
