package com.example.savingsalt.badge.service;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.member.domain.dto.RepresentativeBadgeSetResDto;
import java.util.List;

public interface BadgeService {

    // 해당 뱃지 조회
    BadgeEntity findById(Long badgeId);

    // 모든 뱃지 정보 조회
    List<BadgeDto> getAllBadges();

    // 회원 챌린지 달성 뱃지 정보 조회
    List<MemberChallengeBadgeResDto> getMemberChallengeBadges(boolean isRepresentative,
        Long memberId);

    // 회원 챌린지 대표 뱃지 등록
    RepresentativeBadgeSetResDto setMemberRepresentativeBadge(Long memberId, String badgeName);

    // 뱃지 생성
    BadgeDto createBadge(BadgeCreateReqDto badgeCreateReqDto, String imageUrl);

    // 뱃지 정보 수정
    BadgeDto updateBadge(Long badgeId, BadgeUpdateReqDto badgeUpdateReqDto);

    // 뱃지 삭제
    void deleteBadge(Long badgeId);
}
