package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import java.util.List;

public interface MemberChallengeService {

    // 회원 챌린지 목록 조회
    List<MemberChallengeDto> getMemberChallenges(Long memberId);

    // 회원 챌린지 생성
    public MemberChallengeCreateReqDto createMemberChallenge(Long memberId, Long ChallengeId,
        MemberChallengeCreateReqDto memberChallengeCreateReqDto);

    // 회원 챌린지 최종 성공 인증
    void completeMemberChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 포기
    void abandonMemberChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 일일 인증
    void certifyDailyMemberChallenge(Long memberId, Long memberChallengeId,
        CertificationChallengeReqDto certificationChallengeReqDto);

    // 모든 회원 챌린지 일일 인증 초기화(오전 12시마다)
    void resetDailyMemberChallengeAuthentication();
}