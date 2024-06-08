package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberChallengeAbandonResDto {

    private ChallengeStatus challengeStatus;
}
