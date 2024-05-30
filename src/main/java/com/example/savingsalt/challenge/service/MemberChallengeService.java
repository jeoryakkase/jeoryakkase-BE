package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import java.util.List;

public interface MemberChallengeService {

    // 회원 챌린지 목록 조회
    List<MemberChallengeDto> getMemberChallenges(Long memberId);

    // 회원 챌린지 생성
    MemberChallengeDto createMemberChallenge(MemberChallengeDto memberChallengeDto);

    // 회원 챌린지 최종 성공 인증
    void authenticateFinalChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 포기
    void abandonMemberChallenge(Long memberChallengeId);

    // 회원 챌린지 일일 인증
    void submitDailyMemberChallenge(Long memberChallengeId);
}