package com.example.savingsalt.challenge.controller;

import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.service.ChallengeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Challenge", description = "Challenge API")
public class ChallengeServiceController {

    private final ChallengeServiceImpl challengeService;

    public ChallengeServiceController(ChallengeServiceImpl challengeService) {
        this.challengeService = challengeService;
    }

    // 챌린지 상세 정보 조회
    @Operation(summary = "Get a Challenge", description = "Gets an existing Challenge information by ID")
    @GetMapping("/challenges/{challengeId}")
    public ResponseEntity<ChallengeDto> getChallenge(
        @Parameter(description = "ID of the challenge") @PathVariable Long challengeId) {
        ChallengeDto challengeDto = challengeService.getChallenge(challengeId);

        return (challengeDto == null) ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(challengeDto);
    }

    // 챌린지 전체 목록 조회
    @Operation(summary = "Get Challenges", description = "Gets all existing Challenge information")
    @GetMapping("/challenges")
    public ResponseEntity<Page<ChallengeReadResDto>> getAllChallenges(
        @Parameter(description = "Page number to challenge") @RequestParam(name = "page", defaultValue = "1") int page,
        @Parameter(description = "Number of challenges per page") @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ChallengeReadResDto> challengesReadResDto =
            challengeService.getAllChallenges(pageable);

        return challengesReadResDto.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(challengesReadResDto);
    }


    // 챌린지 키워드 검색
    @Operation(summary = "Get Challenges", description = "Gets all existing Challenge information by keyword")
    @GetMapping("/challenges/search")
    public ResponseEntity<Page<ChallengeReadResDto>> getAllChallenges(
        @Parameter(description = "Challenge search keyword") @RequestParam(name = "keyword", defaultValue = "") String keyword,
        @Parameter(description = "Page number to challenge") @RequestParam(name = "page", defaultValue = "1") int page,
        @Parameter(description = "Number of challenges per page") @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ChallengeReadResDto> challengesReadResDto =
            challengeService.searchChallengesByKeyword(keyword, pageable);

        return challengesReadResDto.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(challengesReadResDto);
    }

    // 챌린지 생성
    @Operation(summary = "Create challenge", description = "Create a challenge")
    @PostMapping("/challenges")
    public ResponseEntity<ChallengeDto> createChallenge(
        @Parameter(description = "Information about the challenge to be created") @RequestBody ChallengeCreateReqDto challengeCreateDto) {
        ChallengeDto createdChallengeDto = challengeService.createChallenge(challengeCreateDto);

        return (createdChallengeDto == null) ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            : ResponseEntity.status(HttpStatus.CREATED).body(createdChallengeDto);
    }

    // 챌린지 수정
    @Operation(summary = "Update challenge", description = "Update a challenge")
    @PutMapping("/challenges/{challengeId}")
    public ResponseEntity<ChallengeDto> updateChallenge(
        @Parameter(description = "ID of the challenge") @PathVariable Long challengeId,
        @Parameter(description = "Information about the challenge to be updated") @RequestBody ChallengeUpdateReqDto challengeUpdateReqDto) {
        ChallengeDto updatedChallengeDto = challengeService.updateChallenge(challengeId,
            challengeUpdateReqDto);

        return (updatedChallengeDto == null) ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            : ResponseEntity.ok(updatedChallengeDto);
    }

    // 챌린지 삭제
    @Operation(summary = "Delete challenge", description = "Delete a challenge")
    @DeleteMapping("/challenges/{challengeId}")
    public ResponseEntity<Void> deleteChallenge(
        @Parameter(description = "ID of the challenge") @PathVariable Long challengeId) {
        challengeService.deleteChallenge(challengeId);

        return ResponseEntity.ok().build();
    }

}
