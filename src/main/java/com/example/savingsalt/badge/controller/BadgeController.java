package com.example.savingsalt.badge.controller;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.service.BadgeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "뱃지 목록 조회", description = "모든 뱃지 목록을 조회하는 API")
    @GetMapping("/badges")
    public ResponseEntity<List<BadgeDto>> getAllBadges() {
        List<BadgeDto> badges = badgeService.getAllBadges();

        return badges.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            : ResponseEntity.ok(badges);
    }

    // 회원 챌린지 달성 뱃지 정보 조회
    @Operation(summary = "회원 챌린지 달성 뱃지 목록 조회", description = "해당 회원의 모든 회원 챌린지 달성 뱃지 목록을 조회하는 API")
    @GetMapping("/members/{memberId}/challenges/badges")
    public ResponseEntity<List<MemberChallengeBadgeResDto>> getMemberChallengeBadges(
        @Parameter(description = "회원 ID") @PathVariable Long memberId) {
        List<MemberChallengeBadgeResDto> memberChallengeBadgeResDto = badgeService.getMemberChallengeBadges(
            memberId);

        return memberChallengeBadgeResDto.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(memberChallengeBadgeResDto);
    }

    // 뱃지 생성
    @Operation(summary = "뱃지 생성", description = "뱃지를 생성하는 API")
    @PostMapping("/badges")
    public ResponseEntity<BadgeDto> createBadge(
        @Parameter(description = "생성할 뱃지의 정보") @Valid @RequestBody BadgeCreateReqDto badgeCreateReqDto) {
        BadgeDto createdBadgeDto = badgeService.createBadge(badgeCreateReqDto);

        return (createdBadgeDto == null) ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            : ResponseEntity.status(HttpStatus.CREATED).body(createdBadgeDto);
    }

    // 뱃지 정보 수정
    @Operation(summary = "뱃지 정보 수정", description = "해당 뱃지의 뱃지 정보를 수정하는 API")
    @PutMapping("/badges/{badgeId}")
    public ResponseEntity<BadgeDto> updateBadge(
        @Parameter(description = "뱃지 ID") @PathVariable Long badgeId,
        @Parameter(description = "수정할 뱃지의 정보") @Valid @RequestBody BadgeUpdateReqDto badgeUpdateReqDto) {
        BadgeDto updatedBadgeDto = badgeService.updateBadge(badgeId, badgeUpdateReqDto);

        return (updatedBadgeDto == null) ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            : ResponseEntity.ok(updatedBadgeDto);
    }

    // 뱃지 삭제
    @Operation(summary = "뱃지 삭제", description = "해당 뱃지의 뱃지 정보를 삭제하는 API")
    @DeleteMapping("/badges/{badgeId}")
    public ResponseEntity<Void> deleteBadge(
        @Parameter(description = "뱃지 ID") @PathVariable Long badgeId) {
        badgeService.deleteBadge(badgeId);

        return ResponseEntity.ok().build();
    }
}
