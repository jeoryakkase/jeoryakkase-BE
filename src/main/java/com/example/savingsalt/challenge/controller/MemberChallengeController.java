package com.example.savingsalt.challenge.controller;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.service.MemberChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
@Tag(name = "MemberChallenge", description = "MemberChallenge API")
public class MemberChallengeController {

    private final MemberChallengeService memberChallengeService;

    // 회원 챌린지 목록 조회
    @Operation(summary = "회원 챌린지 목록 조회", description = "모든 회원 챌린지를 조회하는 API")
    @GetMapping("/members/{memberId}/challenges")
    public ResponseEntity<List<MemberChallengeWithCertifyAndChallengeResDto>> getAllMemberChallenges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberChallengeWithCertifyAndChallengeResDto> memberChallengeWithCertifyAndChallengeResDtos = memberChallengeService.getMemberChallenges(
            memberId);

        return memberChallengeWithCertifyAndChallengeResDtos.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            : ResponseEntity.ok(memberChallengeWithCertifyAndChallengeResDtos);
    }

    // 회원 챌린지 생성
    @Operation(summary = "회원 챌린지 생성", description = "회원 아이디, 챌린지 아이디를 이용해서 회원 챌린지 생성하는 API")
    @PostMapping("/members/{memberId}/challenges/{challengeId}")
    public ResponseEntity<MemberChallengeCreateReqDto> createMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the challenge") @PathVariable Long challengeId,
        @RequestBody MemberChallengeCreateReqDto memberChallengeCreateReqDto) {

        MemberChallengeCreateReqDto createdMemberChallengeCreateReqDto = memberChallengeService.createMemberChallenge(
            memberId, challengeId, memberChallengeCreateReqDto);

        if (createdMemberChallengeCreateReqDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdMemberChallengeCreateReqDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 회원 챌린지 일일 인증 생성
    @Operation(summary = "회원 챌린지 일일 인증 생성", description = "회원 챌린지를 인증 상태로 바꾸고 챌린지 인증 컬럼을 DTO에 맞게 생성하는 API")
    @PostMapping("/members/{memberId}/challenges/{memberChallengeId}/certify")
    public ResponseEntity<Void> certifyDailyMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId,
        @RequestBody CertificationChallengeReqDto certificationChallengeReqDto) {
        memberChallengeService.certifyDailyMemberChallenge(memberId, memberChallengeId,
            certificationChallengeReqDto);

        return ResponseEntity.ok().build();
    }

    // 회원 챌린지 성공
    @Operation(summary = "회원 챌린지 성공", description = "회원 챌린지 목표 금액 달성 및 목표 횟수 달성 시 회원 챌린지가 성공하는 API")
    @PutMapping("/members/{memberId}/challenges/{memberChallengeId}/complete")
    public ResponseEntity<Void> completeMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId) {
        memberChallengeService.completeMemberChallenge(memberId, memberChallengeId);

        return ResponseEntity.ok().build();
    }

    // 회원 챌린지 포기
    @Operation(summary = "회원 챌린지 포기", description = "회원 챌린지를 포기 상태로 바꾸는 API")
    @PutMapping("/members/{memberId}/challenges/{memberChallengeId}/abandon")
    public ResponseEntity<Void> abandonMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId) {
        memberChallengeService.abandonMemberChallenge(memberId, memberChallengeId);

        return ResponseEntity.ok().build();
    }

    // 모든 회원 챌린지 일일 인증 초기화
    @Operation(summary = "모든 회원 챌린지 일일 인증 초기화", description = "오전 12시마다 모든 회원의 회원 챌린지가 미인증 상태로 초기화 하는 API")
    @PutMapping("/members")
    public ResponseEntity<Void> resetMemberChallengeCertification() {
        memberChallengeService.resetDailyMemberChallengeAuthentication();

        return ResponseEntity.ok().build();
    }
}
