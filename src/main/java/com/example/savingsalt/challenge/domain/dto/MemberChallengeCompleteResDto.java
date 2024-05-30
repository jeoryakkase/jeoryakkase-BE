package com.example.savingsalt.challenge.domain.dto;

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
public class MemberChallengeCompleteResDto {

    private String challengeTitle;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String challengeTerm;

    private String badgeImage;
}
