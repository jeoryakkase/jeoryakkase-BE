package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeDifficulty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeCreateReqDto {

    private String challengeTitle;

    private String challengeDesc;

    private int challengeGoal;

    private int challengeCount;

    private String challengeType;

    private String challengeTerm;

    private ChallengeDifficulty challengeDifficulty;

    private String authContent;

    private Long badgeId;

}
