package com.example.savingsalt.goal.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.savingsalt.goal.domain.dto.GoalCertificationCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationResponseDto;
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

        // 이미지 업로드 처리
        String imageUrl = uploadImageToS3(image);

        if (imageUrl == null) {
            // 업로드 실패 시 400 Bad Request 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        goalCertificationCreateReqDto.setCertificationImageUrl(imageUrl); // 이미지 URL 설정

        // 인증 생성 서비스 호출
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
    public ResponseEntity<Page<GoalCertificationResponseDto>> getCertificationsByGoal(
        @Parameter(description = "인증을 조회할 목표의 ID", required = true) @PathVariable Long goalId,
        @AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        Page<GoalCertificationResponseDto> certifications = certificationService.getCertificationsByGoal(goalId, userDetails, pageable);
        return ResponseEntity.ok(certifications);
    }

    // S3에 이미지 업로드하는 메서드
    private String uploadImageToS3(MultipartFile image) {

        if (image.isEmpty()) {
            logger.warn("이미지 파일이 비어있습니다.");
            return null;
        }

        String imageUrl = null;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = image.getOriginalFilename() + "/" + timestamp; // 파일 이름에 타임스탬프 추가

            amazonS3Client.putObject(new PutObjectRequest(
                "my.eliceproject.s3.bucket",
                fileName,
                image.getInputStream(),
                metadata
            ));

            imageUrl = String.format("https://s3.ap-southeast-2.amazonaws.com/%s/%s",
                "my.eliceproject.s3.bucket", fileName);
        } catch (IOException e) {
            logger.error("이미지 업로드 중 오류 발생", e); // 예외 로깅
        }
        return imageUrl;
    }
}
