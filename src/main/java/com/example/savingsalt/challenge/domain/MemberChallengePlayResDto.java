package com.example.savingsalt.challenge.domain;

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
public class MemberChallengePlayResDto {

    private String challengeTitle;

    private String certificationImage;

    private Boolean challengeProgressStatus;

    private String challengeTerm;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
