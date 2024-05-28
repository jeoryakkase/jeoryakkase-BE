package com.example.savingsalt.challenge.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberChallengePlayResDto {

    private String challengeTitle;

    private String certificationImage;

    private Boolean challengeProgressStatus;

    private String challengeTerm;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
