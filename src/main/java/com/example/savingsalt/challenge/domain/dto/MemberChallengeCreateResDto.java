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
public class MemberChallengeCreateResDto {

    private ChallengeStatus challengeStatus;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer successCount;

    private Integer challengeCount;

    private Integer totalSaveMoney;
}
