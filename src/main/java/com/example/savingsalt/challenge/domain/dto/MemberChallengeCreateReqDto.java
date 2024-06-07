package com.example.savingsalt.challenge.domain.dto;


import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
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
public class MemberChallengeCreateReqDto {

    private LocalDateTime certifyDate;

    private ChallengeStatus challengeStatus;

    private Integer successConut;

    private Integer challengeConut;

    private Integer totalSaveMoney;
}
