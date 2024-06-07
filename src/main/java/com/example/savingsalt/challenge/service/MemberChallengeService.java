package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeAbandonResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCompleteReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeJoinResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import java.util.List;

public interface MemberChallengeService {

    // 회원 챌린지 목록 조회
    List<MemberChallengeWithCertifyAndChallengeResDto> getMemberChallenges(Long memberId);

    // 회원 챌린지 생성
    MemberChallengeCreateResDto createMemberChallenge(Long memberId, Long ChallengeId);

    // 회원 챌린지 최종 성공 인증
    MemberChallengeCompleteReqDto completeMemberChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 포기
    MemberChallengeAbandonResDto abandonMemberChallenge(Long memberId, Long memberChallengeId);

    // 회원 챌린지 일일 인증
    MemberChallengeDto certifyDailyMemberChallenge(Long memberId, Long memberChallengeId,
        CertificationChallengeReqDto certificationChallengeReqDto);

    // 모든 회원 챌린지 일일 인증 초기화(오전 12시마다)
    void resetDailyMemberChallengeAuthentication();

    // 참여 중인 챌린지 목록 조회
    List<MemberChallengeJoinResDto> getJoiningMemberChallenge(Long memberId);
}