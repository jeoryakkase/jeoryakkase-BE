package com.example.savingsalt.goal.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.example.savingsalt.goal.domain.dto.GoalCertificationCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationResponseDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationStatisticsResDto;
import com.example.savingsalt.goal.service.GoalCertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "목표 인증", description = "인증 내용을 위한 목표 카테고리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GoalCertificationController {

    private final GoalCertificationService certificationService;
    private final AmazonS3 amazonS3Client; // S3 클라이언트 추가
    private static final Logger logger = LoggerFactory.getLogger(GoalCertificationController.class);

    @Operation(summary = "목표 인증 생성", description = "특정 목표에 대한 인증을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "인증이 성공적으로 생성됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalCertificationResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "목표를 찾을 수 없음",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/goals/{goalId}/certifications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoalCertificationResponseDto> createCertification(
        @Parameter(description = "인증을 추가할 목표의 ID", required = true) @PathVariable Long goalId,
        @Valid @RequestPart("goalCertificationCreateReqDto") GoalCertificationCreateReqDto goalCertificationCreateReqDto,
        @RequestPart("image") MultipartFile image,  // MultipartFile 추가
        @AuthenticationPrincipal UserDetails userDetails) {

        try {
            GoalCertificationResponseDto responseDto = certificationService.createCertification(
                goalCertificationCreateReqDto, image, userDetails, goalId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (IOException e) {
            logger.error("Failed to upload image or create certification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 목표 인증 삭제
    @Operation(summary = "목표 인증 삭제", description = "특정 목표의 인증을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "인증이 성공적으로 삭제됨"),
        @ApiResponse(responseCode = "404", description = "인증을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/goals/{goalId}/certifications/{certificationId}")
    public ResponseEntity<Void> deleteCertification(
        @Parameter(description = "인증이 삭제될 목표의 ID", required = true) @PathVariable Long goalId,
        @Parameter(description = "삭제할 목표 인증의 ID", required = true) @PathVariable Long certificationId,
        @AuthenticationPrincipal UserDetails userDetails) {
        certificationService.deleteCertification(goalId, certificationId, userDetails);
        return ResponseEntity.noContent().build();
    }

    // 특정 목표의 모든 인증 조회
    @Operation(summary = "목표 인증 조회", description = "특정 목표의 모든 인증을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증이 성공적으로 조회됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalCertificationResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "목표를 찾을 수 없음",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/goals/{goalId}/certifications")
    public ResponseEntity<Page<GoalCertificationResponseDto>> getCertificationsByGoal(
        @Parameter(description = "인증을 조회할 목표의 ID", required = true) @PathVariable Long goalId,
        @AuthenticationPrincipal UserDetails userDetails,
        @Parameter(description = "페이지네이션 파라미터: 페이지 번호(page), 페이지 크기(size), 정렬(sort)") Pageable pageable) {
        Page<GoalCertificationResponseDto> certifications = certificationService.getCertificationsByGoal(
            goalId, userDetails, pageable);
        return ResponseEntity.ok(certifications);
    }

    // 목표 인증 통계
    @Operation(summary = "모든 목표 인증 통계 조회", description = "사용자의 모든 목표 인증에 대한 일일, 월간 및 총 인증 금액을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 통계가 성공적으로 조회됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalCertificationStatisticsResDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/goals/certifications/statistics")
    public ResponseEntity<GoalCertificationStatisticsResDto> getAllCertificationStatistics(
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            GoalCertificationStatisticsResDto statistics = certificationService.getTotalCertificationStatistics(
                userDetails);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Error retrieving all certification statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
