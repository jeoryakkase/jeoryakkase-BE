package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberChallengeWithCertifyAndChallengeResDto {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime certifyDate;

    private ChallengeStatus challengeStatus;

    private Boolean isTodayCertification;

    private Integer challengeCount;

    private Integer successCount;

    private String challengeComment;

    private Integer totalSaveMoney;

    private List<CertificationChallengeDto> certificationChallengeDtos;

    private ChallengeDto challengeDto;

}
