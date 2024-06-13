package com.example.savingsalt.goal.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.savingsalt.goal.domain.dto.GoalCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalMainResponseDto;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "목표", description = "인증 내용을 위한 목표 카테고리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GoalController {

    private final GoalService goalService;
    private final AmazonS3 amazonS3Client; // S3 클라이언트 추가
    private static final Logger logger = LoggerFactory.getLogger(GoalCertificationController.class);

    @Operation(summary = "새로운 목표 생성", description = "제공된 데이터를 기반으로 새로운 목표를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "목표가 성공적으로 생성됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/goals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoalResponseDto> createGoal(
        @Valid @Parameter(description = "목표 생성을 위한 요청 데이터", required = true, schema = @Schema(implementation = GoalCreateReqDto.class))
        @RequestPart("goalCreateReqDto") GoalCreateReqDto goalCreateReqDto,
        @RequestPart("image") MultipartFile image,  // MultipartFile 추가
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자의 정보", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails) {

        try {
            GoalResponseDto createdGoal = goalService.createGoal(goalCreateReqDto, image, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
        } catch (Exception e) {
            logger.error("Error creating goal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 특정 사용자의 모든 목표 조회
    @Operation(summary = "모든 목표 조회", description = "현재 사용자의 모든 목표를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "목표가 성공적으로 조회됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "목표를 찾을 수 없음",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/goals")
    public ResponseEntity<Page<GoalResponseDto>> getAllGoals(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자의 정보", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
        @Parameter(description = "페이지네이션 파라미터: 페이지 번호(page), 페이지 크기(size), 정렬(sort)") Pageable pageable) {
        Page<GoalResponseDto> goals = goalService.getAllGoals(userDetails, pageable);
        return ResponseEntity.ok(goals);
    }

    // 목표 상태를 포기 상태로 업데이트
    @Operation(summary = "목표 포기", description = "특정 목표의 상태를 포기 상태로 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "목표 상태가 성공적으로 포기됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "권한 없음",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "목표를 찾을 수 없음",
            content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/goals/{id}")
    public ResponseEntity<GoalResponseDto> giveUpGoal(
        @Parameter(description = "포기할 목표의 ID", required = true) @PathVariable Long id,
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자의 정보", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails) {
        GoalResponseDto updatedGoal = goalService.giveUpGoal(id, userDetails);
        return ResponseEntity.ok(updatedGoal);
    }

    @Operation(summary = "Retrieve goal statuses", description = "Retrieves statuses for multiple goals based on certain criteria.")
    @GetMapping("/goals/status")
    public ResponseEntity<List<GoalMainResponseDto>> getGoalStatuses() {
        List<GoalMainResponseDto> goals = goalService.getGoalStatuses();
        return ResponseEntity.ok(goals);
    }
}
