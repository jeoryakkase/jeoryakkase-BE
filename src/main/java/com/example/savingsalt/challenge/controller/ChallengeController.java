package com.example.savingsalt.challenge.controller;

import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeMainResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeJoinResDto;
import com.example.savingsalt.challenge.service.ChallengeServiceImpl;
import com.example.savingsalt.challenge.service.MemberChallengeServiceImpl;
import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.dto.MemberDto;
import com.example.savingsalt.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
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
public class ChallengeController {

    private final ChallengeServiceImpl challengeService;
    private final MemberChallengeServiceImpl memberChallengeService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public ChallengeController(ChallengeServiceImpl challengeService,
        MemberChallengeServiceImpl memberChallengeService, JwtTokenProvider jwtTokenProvider,
        MemberService memberService) {
        this.challengeService = challengeService;
        this.memberChallengeService = memberChallengeService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    // 챌린지 상세 정보 조회
    @Operation(summary = "챌린지 상세 정보 조회", description = "해당 챌린지의 챌린지 상세 정보를 조회하는 API")
    @GetMapping("/challenges/{challengeId}")
    public ResponseEntity<ChallengeDto> getChallenge(
        @Parameter(description = "챌린지 ID") @PathVariable Long challengeId) {
        ChallengeDto challengesDto = challengeService.getChallenge(challengeId);

        return (challengesDto == null) ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(challengesDto);
    }

    // 챌린지 전체 목록 및 키워드 검색(인기 챌린지 목록 조회 포함)
    @Operation(summary = "챌린지 목록 전체 조회", description = "모든 챌린지 목록을 조회하는 API(keyword에 \"Popularity\" 입력시 인기 챌린지들 조회)")
    @GetMapping("/challenges/all")
    public ResponseEntity<Page<ChallengeReadResDto>> getAllChallenges(
        @Parameter(description = "검색할 키워드(챌린지 유형(\"Goal\", \"Count\"))") @RequestParam(name = "keyword", defaultValue = "") String keyword,
        @Parameter(description = "페이지 번호") @RequestParam(name = "page", defaultValue = "1") int page,
        @Parameter(description = "페이지당 챌린지 수") @RequestParam(name = "size", defaultValue = "5") int size) {

        Page<ChallengeReadResDto> challengesReadResDto =
            challengeService.getAllChallenges(keyword, page, size);

        return challengesReadResDto.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(challengesReadResDto);
    }

    // 챌린지 메인페이지 조회
    @Operation(summary = "챌린지 메인 페이지 조회", description = "회원 참여 챌린지(로그인 시), 전체 챌린지, 인기 챌린지 합쳐서 조회)")
    @GetMapping("/challenges")
    public ResponseEntity<ChallengeMainResDto> getChallengeMain(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request) {
        ChallengeMainResDto challengeMainResDto = challengeService.getChallengeMain();

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null || jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            MemberDto memberDto = memberService.findMemberByEmail(email);
            List<MemberChallengeJoinResDto> memberChallengesJoinResDto = memberChallengeService.getJoiningMemberChallenge(
                memberDto.getId());

            List<MemberChallengeJoinResDto> memberChallengesJoinSubList =
                memberChallengesJoinResDto.size() >= 3 ? memberChallengesJoinResDto.subList(0, 3)
                    : memberChallengesJoinResDto;

            challengeMainResDto = challengeMainResDto.toBuilder()
                .memberChallengesJoinResDto(memberChallengesJoinSubList)
                .build();
        }
        return (challengeMainResDto == null) ? ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build() : ResponseEntity.ok(challengeMainResDto);
    }

    // 챌린지 생성
    @Operation(summary = "챌린지 생성", description = "챌린지를 생성하는 API")
    @PostMapping("/challenges")
    public ResponseEntity<ChallengeDto> createChallenge(
        @Parameter(description = "생성할 챌린지의 정보") @Valid @RequestBody ChallengeCreateReqDto challengeCreateDto) {
        ChallengeDto createdChallengeDto = challengeService.createChallenge(challengeCreateDto);

        return (createdChallengeDto == null) ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            : ResponseEntity.status(HttpStatus.CREATED).body(createdChallengeDto);
    }

    // 챌린지 수정
    @Operation(summary = "챌린지 수정", description = "해당 챌린지의 챌린지 정보를 수정하는 API")
    @PutMapping("/challenges/{challengeId}")
    public ResponseEntity<ChallengeDto> updateChallenge(
        @Parameter(description = "챌린지 ID") @PathVariable Long challengeId,
        @Parameter(description = "수정할 챌린지의 정보") @RequestBody ChallengeUpdateReqDto challengeUpdateReqDto) {
        ChallengeDto updatedChallengeDto = challengeService.updateChallenge(challengeId,
            challengeUpdateReqDto);

        return (updatedChallengeDto == null) ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            : ResponseEntity.ok(updatedChallengeDto);
    }

    // 챌린지 삭제
    @Operation(summary = "챌린지 삭제", description = "해당 챌린지의 챌린지 정보를 삭제하는 API")
    @DeleteMapping("/challenges/{challengeId}")
    public ResponseEntity<Void> deleteChallenge(
        @Parameter(description = "챌린지 ID") @PathVariable Long challengeId) {
        challengeService.deleteChallenge(challengeId);

        return ResponseEntity.ok().build();
    }
}
