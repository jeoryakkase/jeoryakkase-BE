package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;

public interface CertificationChallengeService {

    // 회원 챌린지 일일 인증 이미지 경로 및 인증 날짜 업로드
    void uploadDailyChallengeImageUrlAndDateTime(Long memberChallngeId, CertificationChallengeReqDto certificationChallengeReqDto);

}
