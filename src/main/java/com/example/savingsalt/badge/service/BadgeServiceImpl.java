package com.example.savingsalt.badge.service;

import com.example.savingsalt.badge.domain.BadgeCreateDto;
import com.example.savingsalt.badge.domain.BadgeDto;
import com.example.savingsalt.badge.domain.BadgeUpdateDto;
import java.util.List;

public interface BadgeServiceImpl {

    // 뱃지 정보 조회
    BadgeDto getAllBadges();

    // 회원 뱃지 정보 조회
    List<BadgeDto> getMemberBadges(Long memberId);

    // 뱃지 생성
    BadgeDto createBadge(BadgeCreateDto badgeCreateDto);

    // 뱃지 정보 수정
    BadgeDto updateBadge(BadgeUpdateDto badgeUpdateDto);

    // 뱃지 삭제
    void deleteBadge(Long badgeId);
}
