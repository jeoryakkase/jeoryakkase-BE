package com.example.savingsalt.badge.controller;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.service.BadgeServiceImpl;
import com.example.savingsalt.config.s3.S3Service;
import com.example.savingsalt.member.domain.dto.RepresentativeBadgeSetResDto;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(name = "Badge", description = "Badge API")
public class BadgeController {

    private final BadgeServiceImpl badgeService;
    private final MemberService memberService;
    private final S3Service s3Service;

    public BadgeController(BadgeServiceImpl badgeService,
        MemberService memberService, S3Service s3Service) {
        this.badgeService = badgeService;
        this.memberService = memberService;
        this.s3Service = s3Service;
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
    @GetMapping("/members/challenges/badges")
    public ResponseEntity<List<MemberChallengeBadgeResDto>> getMemberChallengeBadges(
        @Parameter(description = "회원 대표 뱃지 검색 유무(true를 하면 대표 뱃지만 보여줌)") @RequestParam(name = "IsRepresentative", defaultValue = "false") boolean isRepresentative,
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request) {
        MemberEntity memberEntity = memberService.getMemberFromRequest(request);

        List<MemberChallengeBadgeResDto> memberChallengeBadgeResDto = badgeService.getMemberChallengeBadges(
            isRepresentative, memberEntity.getId());

        return memberChallengeBadgeResDto.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(memberChallengeBadgeResDto);
    }

    // 회원 챌린지 대표 뱃지 등록
    @Operation(summary = "회원 챌린지 대표 뱃지 등록", description = "해당 회원의 모든 챌린지 성공 뱃지중에 대표 뱃지를 등록하는 API")
    @PutMapping("/members/challenges/badges")
    public ResponseEntity<RepresentativeBadgeSetResDto> setMemberRepresentativeBadge(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request,
        @Parameter(description = "대표 뱃지로 지정할 뱃지 이름") @RequestParam String badgeName) {
        MemberEntity memberEntity = memberService.getMemberFromRequest(request);

        RepresentativeBadgeSetResDto memberRepresentativeBadge = badgeService.setMemberRepresentativeBadge(
            memberEntity.getId(), badgeName);

        return (memberRepresentativeBadge == null) ? ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .build()
            : ResponseEntity.ok(memberRepresentativeBadge);
    }

    // 뱃지 생성
    @Operation(summary = "뱃지 생성", description = "뱃지를 생성하는 API")
    @PostMapping(value = "/badges", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BadgeDto> createBadge(
        @Parameter(description = "생성할 뱃지의 정보") @Valid @RequestPart BadgeCreateReqDto badgeCreateReqDto,
        @RequestPart("uploadFile") MultipartFile multipartFile) throws IOException {
        String imageUrl = s3Service.upload(multipartFile);

        BadgeDto createdBadgeDto = badgeService.createBadge(badgeCreateReqDto, imageUrl);

        return (createdBadgeDto == null) ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            : ResponseEntity.status(HttpStatus.CREATED).body(createdBadgeDto);
    }

    // 뱃지 정보 수정
    @Operation(summary = "뱃지 정보 수정", description = "해당 뱃지의 뱃지 정보를 수정하는 API")
    @PutMapping(value = "/badges/{badgeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BadgeDto> updateBadge(
        @Parameter(description = "뱃지 ID") @PathVariable Long badgeId,
        @Parameter(description = "수정할 뱃지의 정보") @Valid @RequestPart BadgeUpdateReqDto badgeUpdateReqDto,
        @RequestPart(value = "uploadFile", required = false) MultipartFile multipartFile) throws IOException {
        String imageUrl = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            imageUrl = s3Service.upload(multipartFile);
        }

        BadgeDto updatedBadgeDto = badgeService.updateBadge(badgeId, badgeUpdateReqDto, imageUrl);

        return (updatedBadgeDto == null) ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
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
