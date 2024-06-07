package com.example.savingsalt.goal.controller;

import com.example.savingsalt.goal.domain.dto.GoalCategoryCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCategoryResDto;
import com.example.savingsalt.goal.service.GoalCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "목표 카테고리", description = "인증 내용을 위한 목표 카테고리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GoalCategoryController {

    private final GoalCategoryService goalCategoryService;

    // 카테고리 생성
    @Operation(summary = "목표 카테고리 생성", description = "새로운 목표 카테고리를 생성한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/goal/category")
    public ResponseEntity<Void> createGoalCategory(
        @Parameter(description = "목표 카테고리의 세부 사항", required = true,
            content = @Content(schema = @Schema(implementation = GoalCategoryCreateReqDto.class)))
        @Valid @RequestBody GoalCategoryCreateReqDto goalCategoryCreateReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername(); // 인증된 사용자 정보
        goalCategoryService.createGoalCategory(goalCategoryCreateReqDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 사용자별 카테고리 조회
    @Operation(summary = "사용자별 카테고리 조회", description = "사용자별로 자신이 작성한 카테고리 인증 내용을 조회할 수 있다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GoalCategoryResDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/goal/categories")
    public ResponseEntity<List<GoalCategoryResDto>> getGoalCategoriesByUser(
        @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername(); // 인증된 사용자 정보
        List<GoalCategoryResDto> goalCategories = goalCategoryService.getGoalCategoriesByUser(
            userEmail);
        return ResponseEntity.ok(goalCategories);
    }
}
