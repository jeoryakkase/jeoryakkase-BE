package com.example.savingsalt.badge.service;

import com.example.savingsalt.badge.domain.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.BadgeDto;
import com.example.savingsalt.badge.domain.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.MemberGoalBadgeResDto;
import java.util.List;

public interface BadgeService {

    // 모든 뱃지 정보 조회
    List<BadgeDto> getAllBadges();

    // 회원 목표 달성 뱃지 정보 조회
    List<MemberGoalBadgeResDto> getMemberGoalBadges(Long memberId);

    // 회원 챌린지 달성 뱃지 정보 조회
    List<MemberChallengeBadgeResDto> getMemberChallengeBadges(Long memberId);

    // 뱃지 생성
    BadgeDto createBadge(BadgeCreateReqDto badgeCreateReqDto);

    // 뱃지 정보 수정
    BadgeDto updateBadge(Long badgeId, BadgeUpdateReqDto badgeUpdateReqDto);

    // 뱃지 삭제
    void deleteBadge(Long badgeId);
}
