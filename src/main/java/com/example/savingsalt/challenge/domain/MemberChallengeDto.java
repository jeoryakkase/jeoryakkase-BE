package com.example.savingsalt.challenge.domain;

import com.example.savingsalt.challenge.domain.MemberChallengeEntity.ChallengeStatus;
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
public class MemberChallengeDto {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime certifyDate;

    private Boolean challengeProgressStatus;

    private ChallengeStatus challengeStatus;

    private Integer challengeConut;

    private Integer challengeTry;

    private String challengeComment;

    private Integer saveMoney;

    private List<CertificationChallengeDto> certificationChallengeDtos;

    private ChallengeDto challengeDto;

}
