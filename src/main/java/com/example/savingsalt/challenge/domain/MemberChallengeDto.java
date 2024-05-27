package com.example.savingsalt.challenge.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberChallengeDto {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime certifyDate;

    private Boolean challengeProgressStatus;

    private Boolean challengeStatus;

    private Integer challengeConut;

    private Integer challengeTry;

    private String challengeComment;

    private Integer saveMoney;

    private List<CertificationChallengeEntity> certificationChallengeEntities;

    private ChallengeEntity challengeEntity;
}
