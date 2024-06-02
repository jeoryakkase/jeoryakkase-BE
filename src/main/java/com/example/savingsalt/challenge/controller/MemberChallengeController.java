package com.example.savingsalt.challenge.controller;

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

    //

}
