package com.example.savingsalt.badge.controller;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.dto.MemberGoalBadgeResDto;
import com.example.savingsalt.badge.service.BadgeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Badge", description = "Badge API")
public class BadgeController {

    private final BadgeServiceImpl badgeService;

    public BadgeController(BadgeServiceImpl badgeService) {
        this.badgeService = badgeService;
    }

    // 모든 뱃지 조회
    @Operation(summary = "Get badges", description = "Gets all existing badge information")
    @GetMapping("/challenges/badges")
    public ResponseEntity<List<BadgeDto>> getAllBadges() {
        List<BadgeDto> badges = badgeService.getAllBadges();

        if (badges != null) {
            return ResponseEntity.ok(badges);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // 회원 목표 달성 뱃지 정보 조회
    @Operation(summary = "Get MemberGoalBadges", description = "Gets all existing memberGoalbadge information")
    @GetMapping("/members/{memberId}/goal/badges")
    public ResponseEntity<List<MemberGoalBadgeResDto>> getMemberGoalBadges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberGoalBadgeResDto> memberGoalBadgesResDto = badgeService.getMemberGoalBadges(
            memberId);

        if (memberGoalBadgesResDto != null) {
            return ResponseEntity.ok(memberGoalBadgesResDto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // 회원 챌린지 달성 뱃지 정보 조회
    @Operation(summary = "Get MemberChallengeBadges", description = "Gets all existing memberChallengeBadge information")
    @GetMapping("/members/{memberId}/challenges/badges")
    public ResponseEntity<List<MemberChallengeBadgeResDto>> getMemberChallengeBadges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberChallengeBadgeResDto> memberChallengeBadgeResDto = badgeService.getMemberChallengeBadges(
            memberId);

        if (memberChallengeBadgeResDto != null) {
            return ResponseEntity.ok(memberChallengeBadgeResDto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // 뱃지 생성
    @Operation(summary = "Create badge", description = "Create a badge")
    @PostMapping("/badges")
    public ResponseEntity<BadgeDto> createBadge(
        @Parameter(description = "Information about the badge to be created") @RequestBody BadgeCreateReqDto badgeCreateReqDto) {
        BadgeDto createdBadgeDto = badgeService.createBadge(badgeCreateReqDto);

        if (createdBadgeDto != null) {
            return ResponseEntity.ok(createdBadgeDto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
