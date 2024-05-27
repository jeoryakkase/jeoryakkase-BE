package com.example.savingsalt.challenge.service;

public interface CertificationChallengeService {

    // 회원 챌린지 일일 인증
    boolean authenticateDailyChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 일일 인증 초기화
    void resetDailyAuthentication(Long memberId, Long memberChallengeId);
}
