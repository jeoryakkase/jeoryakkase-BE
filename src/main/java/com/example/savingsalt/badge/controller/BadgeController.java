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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
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

        if (!ObjectUtils.isEmpty(badges)) {
            return ResponseEntity.ok(badges);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // 회원 목표 달성 뱃지 정보 조회
    @Operation(summary = "Get MemberGoalBadges", description = "Gets all existing memberGoalbadge information")
    @GetMapping("/members/{memberId}/goals/badges")
    public ResponseEntity<List<MemberGoalBadgeResDto>> getMemberGoalBadges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberGoalBadgeResDto> memberGoalBadgesResDto = badgeService.getMemberGoalBadges(
            memberId);

        if (!ObjectUtils.isEmpty(memberGoalBadgesResDto)) {
            return ResponseEntity.ok(memberGoalBadgesResDto);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // 회원 챌린지 달성 뱃지 정보 조회
    @Operation(summary = "Get MemberChallengeBadges", description = "Gets all existing memberChallengeBadge information")
    @GetMapping("/members/{memberId}/challenges/badges")
    public ResponseEntity<List<MemberChallengeBadgeResDto>> getMemberChallengeBadges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberChallengeBadgeResDto> memberChallengeBadgeResDto = badgeService.getMemberChallengeBadges(
            memberId);

        if (!ObjectUtils.isEmpty(memberChallengeBadgeResDto)) {
            return ResponseEntity.ok(memberChallengeBadgeResDto);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // 뱃지 생성
    @Operation(summary = "Create badge", description = "Create a badge")
    @PostMapping("/badges")
    public ResponseEntity<BadgeDto> createBadge(
        @Parameter(description = "Information about the badge to be created") @RequestBody BadgeCreateReqDto badgeCreateReqDto) {
        BadgeDto createdBadgeDto = badgeService.createBadge(badgeCreateReqDto);

        if (createdBadgeDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBadgeDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 회원 목표 달성 뱃지 생성
    @Operation(summary = "Create memberGoalbadge", description = "Create a memberGoalbadge")
    @PostMapping("/members/{memberId}/goals/badges{badgeId}")
    public ResponseEntity<BadgeDto> createMemberGoalBadge(
        @Parameter(description = "ID of the badge") @PathVariable Long badgeId,
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        BadgeDto createdMemberGoalBadgeDto = badgeService.createMemberGoalBadge(badgeId, memberId);

        if (createdMemberGoalBadgeDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMemberGoalBadgeDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 뱃지 정보 수정
    @Operation(summary = "Update badge", description = "Update a badge")
    @PutMapping("/badges/{badgeId}")
    public ResponseEntity<BadgeDto> updateBadge(
        @Parameter(description = "ID of the badge") @PathVariable Long badgeId,
        @Parameter(description = "Information about the badge to be updated") @RequestBody BadgeUpdateReqDto badgeUpdateReqDto) {
        BadgeDto updatedBadgeDto = badgeService.updateBadge(badgeId, badgeUpdateReqDto);

        if (updatedBadgeDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedBadgeDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 뱃지 삭제
    @Operation(summary = "Delete badge", description = "Delete a badge")
    @DeleteMapping("/badges/{badgeId}")
    public ResponseEntity<Void> deleteBadge(
        @Parameter(description = "ID of the badge") @PathVariable Long badgeId) {

        badgeService.deleteBadge(badgeId);

        return ResponseEntity.ok().build();
    }
}
