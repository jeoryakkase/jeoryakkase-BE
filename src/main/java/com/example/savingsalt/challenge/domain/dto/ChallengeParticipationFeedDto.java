package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.member.domain.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class ChallengeParticipationFeedDto {

    private MemberDto memberDto;

    private MemberChallengeDto memberChallengeDto;

    private CertificationChallengeDto certificationChallengeDto;
}
