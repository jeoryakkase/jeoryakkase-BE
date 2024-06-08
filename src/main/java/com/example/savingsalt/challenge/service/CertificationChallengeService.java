package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;

public interface CertificationChallengeService {

    // 회원 챌린지 일일 인증 생성
    public CertificationChallengeDto createCertificationChallenge(
        MemberChallengeEntity memberChallengeEntity,
        CertificationChallengeReqDto certificationChallengeReqDto);
}
