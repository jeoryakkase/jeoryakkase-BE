package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import java.util.List;

public interface CertificationChallengeService {

    // 챌린지 인증 생성
    CertificationChallengeDto createCertificationChallenge(
        MemberChallengeEntity memberChallengeEntity,
        CertificationChallengeReqDto certificationChallengeReqDto, List<String> imageUrls);

    // 챌린지 인증 삭제
    void deleteCertificationChallengeById(Long CertificationChallengeId);

    // 챌린지 인증 조회
    List<CertificationChallengeDto> getCertifiCationChallenges(
        MemberChallengeEntity memberChallengeEntity);

    // 선택된 챌린지 인증 전체 조회
    List<CertificationChallengeDto> selectedChallengeGetCertifiCationChallenges(Long challengeId);
}
