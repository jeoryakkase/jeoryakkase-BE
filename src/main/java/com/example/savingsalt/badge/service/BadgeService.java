package com.example.savingsalt.badge.service;

import com.example.savingsalt.badge.domain.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.BadgeDto;
import com.example.savingsalt.badge.domain.BadgeUpdateReqDto;
import java.util.List;

public interface BadgeService {

    // 뱃지 정보 조회
    BadgeDto getAllBadges();

    // 회원 뱃지 정보 조회
    List<BadgeDto> getMemberBadges(Long memberId);

    // 뱃지 생성
    BadgeDto createBadge(BadgeCreateReqDto badgeCreateReqDto);

    // 뱃지 정보 수정
    BadgeDto updateBadge(BadgeUpdateReqDto badgeUpdateReqDto);

    // 뱃지 삭제
    void deleteBadge(Long badgeId);
}
