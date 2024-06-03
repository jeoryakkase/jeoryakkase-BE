package com.example.savingsalt.goal.service;

import com.example.savingsalt.goal.domain.dto.GoalCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.domain.dto.GoalUpdateReqDto;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.exception.GoalNotFoundException;
import com.example.savingsalt.goal.exception.InvalidGoalRequestException;
import com.example.savingsalt.goal.repository.GoalRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalService {

    private final GoalRepository goalRepository;

    // 목표를 리포지토리에 저장
    // TODO: 유효성 검사 및 로그 추가 할 것
    public GoalResponseDto createGoal(GoalCreateReqDto goalCreateReqDto) {
        GoalEntity goalEntity = goalCreateReqDto.toEntity(goalCreateReqDto);

        GoalEntity savedGoal = goalRepository.save(goalEntity);
        return GoalResponseDto.fromEntity(savedGoal);
    }

    // 진행중인 모든 목표를 가져오기
    // TODO: 유효성 검사 및 로그 추가 할 것
    public List<GoalResponseDto> getAllGoals() {
        List<GoalEntity> goalEntities = goalRepository.findAll();
        return goalEntities.stream()
            .map(GoalResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    // 진행중인 목표 수정
    public GoalResponseDto updateGoal(Long id, GoalUpdateReqDto goalUpdateReqDto) {
        Optional<GoalEntity> optionalGoalEntity = goalRepository.findById(id);

        if (optionalGoalEntity.isEmpty()) {
            throw new GoalNotFoundException("해당하는 목표를 찾을 수 없습니다.");
        }

        GoalEntity existingGoalEntity = optionalGoalEntity.get();
        GoalEntity updatedGoalEntity = goalUpdateReqDto.toEntity(id, existingGoalEntity);

        GoalEntity savedGoal = goalRepository.save(updatedGoalEntity);
        return GoalResponseDto.fromEntity(savedGoal);

    }
}
