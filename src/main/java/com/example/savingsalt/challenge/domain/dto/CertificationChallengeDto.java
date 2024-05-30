package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CertificationChallengeDto {

    private LocalDateTime certificationDate;

    private String challengeImg;

    private MemberChallengeEntity memberChallengeEntity;
}
