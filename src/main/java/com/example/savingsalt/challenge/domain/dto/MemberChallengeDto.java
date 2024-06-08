package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class MemberChallengeDto {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime successDate;

    private ChallengeStatus challengeStatus;

    private Boolean isTodayCertification;

    private Integer authCount;

    private Integer successCount;

    private String challengeComment;

    private Integer totalSaveMoney;

    private CertificationChallengeDto certificationChallengeDto;
}
