package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.ChallengeEntity;
import com.example.savingsalt.challenge.domain.MemberChallengeDto;
import java.util.List;

public interface MemberChallengeService {

    // 회원 챌린지 목록 조회
    List<MemberChallengeDto> getMemberChallenges(Long memberId);

    // 회원 챌린지 조회
    MemberChallengeDto getMemberChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 생성
    MemberChallengeDto createMemberChallenge(Long memberId, Long challengeId);

    // 회원 챌린지 최종 성공 인증
    boolean authenticateFinalChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 포기
    void abandonMemberChallenge(Long memberId, Long memberChallengeId);

    // 실시간 인기 챌린지 조회
    List<ChallengeEntity> getPopularityChallenges();
}
