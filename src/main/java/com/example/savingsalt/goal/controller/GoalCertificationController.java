package com.example.savingsalt.goal.controller;

import com.example.savingsalt.goal.domain.dto.GoalCertificationCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationResponseDto;
import com.example.savingsalt.goal.service.GoalCertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GoalCertificationController {

    private final GoalCertificationService certificationService;

    @Operation(summary = "목표 인증 생성", description = "특정 목표에 대한 인증을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "인증이 성공적으로 생성됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalCertificationResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "목표를 찾을 수 없음",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/goals/{goalId}/certifications")
    public ResponseEntity<GoalCertificationResponseDto> createCertification(
        @Parameter(description = "인증을 추가할 목표의 ID", required = true) @PathVariable Long goalId,
        @Valid @RequestBody GoalCertificationCreateReqDto goalCertificationCreateReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        GoalCertificationResponseDto responseDto = certificationService.createCertification(
            goalCertificationCreateReqDto, userDetails, goalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
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
    public ResponseEntity<List<GoalCertificationResponseDto>> getCertificationsByGoal(
        @Parameter(description = "인증을 조회할 목표의 ID", required = true) @PathVariable Long goalId,
        @AuthenticationPrincipal UserDetails userDetails) {
        List<GoalCertificationResponseDto> certifications = certificationService.getCertificationsByGoal(
            goalId, userDetails);
        return ResponseEntity.ok(certifications);
    }
}
