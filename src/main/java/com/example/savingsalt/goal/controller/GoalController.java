package com.example.savingsalt.goal.controller;

import com.example.savingsalt.goal.domain.dto.GoalCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.domain.dto.GoalUpdateReqDto;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.service.GoalService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GoalController {

    private final GoalService goalService;

    @PostMapping("/goals")
    public ResponseEntity<GoalResponseDto> createGoal(
        @RequestBody GoalCreateReqDto goalCreateReqDto) {
        GoalResponseDto created = goalService.createGoal(goalCreateReqDto);
        return (created != null) ?
            ResponseEntity.status(HttpStatus.CREATED).body(created) :
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/goals")
    public ResponseEntity<List<GoalResponseDto>> getAllGoals() {
        List<GoalResponseDto> goals = goalService.getAllGoals();
        return (goals != null && !goals.isEmpty()) ?
            ResponseEntity.ok(goals) :
            ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/goals/{id}")
    public ResponseEntity<GoalResponseDto> updateGoal(
        @PathVariable Long id,
        @RequestBody GoalUpdateReqDto goalUpdateReqDto) {
        GoalResponseDto updated = goalService.updateGoal(id, goalUpdateReqDto);
        return (updated != null) ?
            ResponseEntity.ok(updated) :
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}