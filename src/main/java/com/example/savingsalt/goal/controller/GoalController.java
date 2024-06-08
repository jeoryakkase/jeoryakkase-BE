package com.example.savingsalt.goal.controller;

import com.example.savingsalt.goal.domain.dto.GoalCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.domain.dto.GoalUpdateReqDto;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GoalController {

    private final GoalService goalService;

    // 목표 생성
    @Operation(summary = "새로운 목표 생성", description = "제공된 데이터를 기반으로 새로운 목표를 생성합니다.")
    @PostMapping("/goals")
    public ResponseEntity<GoalResponseDto> createGoal(
        @Valid @Parameter(description = "목표 생성을 위한 요청 데이터", required = true, schema = @Schema(implementation = GoalCreateReqDto.class))
        @RequestBody GoalCreateReqDto goalCreateReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        GoalResponseDto created = goalService.createGoal(goalCreateReqDto, userDetails);
        return (created != null) ? ResponseEntity.status(HttpStatus.CREATED).body(created)
            : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

//    @Operation(summary = "모든 목표 조회", description = "모든 목표를 검색하여 가져옵니다.")
//    @GetMapping("/goals")
//    public ResponseEntity<List<GoalResponseDto>> getAllGoals() {
//        List<GoalResponseDto> goals = goalService.getAllGoals();
//        return (goals != null && !goals.isEmpty()) ? ResponseEntity.ok(goals)
//            : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

//    @Operation(summary = "목표 수정", description = "제공된 데이터를 기반으로 기존 목표를 업데이트합니다.")
//    @PutMapping("/goals/{id}")
//    public ResponseEntity<GoalResponseDto> updateGoal(
//        @Parameter(description = "수정할 목표의 고유 ID", required = true, example = "1")
//        @PathVariable Long id,
//        @Valid @Parameter(description = "목표 수정을 위한 요청 데이터", required = true, schema = @Schema(implementation = GoalUpdateReqDto.class))
//        @RequestBody GoalUpdateReqDto goalUpdateReqDto) {
//        GoalResponseDto updated = goalService.updateGoal(id, goalUpdateReqDto);
//        return (updated != null) ? ResponseEntity.ok(updated)
//            : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
}
