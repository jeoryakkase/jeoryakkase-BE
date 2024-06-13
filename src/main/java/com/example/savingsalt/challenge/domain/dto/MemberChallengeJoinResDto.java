package com.example.savingsalt.challenge.domain.dto;

import java.time.LocalDate;
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
public class MemberChallengeJoinResDto {

    private String challengeTtile;

    private String challengeTerm;

    private Boolean isTodayCertification;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long effectiveDate;

    private List<CertificationChallengeDto> certificationChallengeDtos;
}
