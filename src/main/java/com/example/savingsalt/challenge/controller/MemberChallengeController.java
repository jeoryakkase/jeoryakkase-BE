package com.example.savingsalt.challenge.controller;

import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
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
    @Operation(summary = "Get member challenges", description = "Gets all existing memberchallenge information")
    @GetMapping("/members/{memberId}/challenges")
    public ResponseEntity<List<MemberChallengeDto>> getAllMemberChallenges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberChallengeDto> memberChallengeDtos = memberChallengeService.getMemberChallenges(
            memberId);

        return memberChallengeDtos.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            : ResponseEntity.ok(memberChallengeDtos);
    }

    // 회원 챌린지 생성
    @Operation(summary = "Create member challenge", description = "Create a member challnenge")
    @PostMapping("/members/{memberId}/challenges")
    public ResponseEntity<MemberChallengeCreateReqDto> createMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @RequestBody MemberChallengeCreateReqDto memberChallengeCreateReqDto) {
        MemberChallengeCreateReqDto createdMemberChallengeCreateReqDto = memberChallengeService.createMemberChallenge(
            memberId, memberChallengeCreateReqDto);

        if (createdMemberChallengeCreateReqDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdMemberChallengeCreateReqDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 회원 챌린지 성공
    @Operation(summary = "Complete member challenge", description = "Complete a member challnenge")
    @PutMapping("/members/{memberId}/challenges/{memberChallengeId}")
    public ResponseEntity<Void> completeMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId) {
        memberChallengeService.completeMemberChallenge(memberId, memberChallengeId);

        return ResponseEntity.ok().build();
    }

    // 회원 챌린지 포기
    @Operation(summary = "Abandon member challenge", description = "Abandon a member challnenge")
    @PutMapping("/members/{memberId}/challenges/{memberChallengeId}")
    public ResponseEntity<Void> abandonMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId) {
        memberChallengeService.abandonMemberChallenge(memberId, memberChallengeId);

        return ResponseEntity.ok().build();
    }


}
