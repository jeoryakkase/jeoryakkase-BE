package com.example.savingsalt.goal.service;

import com.example.savingsalt.goal.domain.dto.GoalCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.domain.dto.GoalUpdateReqDto;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.goal.exception.GoalNotFoundException;
import com.example.savingsalt.goal.exception.InvalidGoalRequestException;
import com.example.savingsalt.goal.exception.MaxProceedingGoalsExceededException;
import com.example.savingsalt.goal.repository.GoalRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    // 목표를 리포지토리에 저장
    public GoalResponseDto createGoal(GoalCreateReqDto goalCreateReqDto, UserDetails userDetails) {
        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        // 진행중인 목표의 개수를 확인
        long proceedingGoalsCount = goalRepository.countByMemberEntityAndGoalStatus(memberEntity,
            GoalStatus.PROCEEDING);

        if (proceedingGoalsCount >= 5) {
            throw new MaxProceedingGoalsExceededException();
        }

        GoalEntity goalEntity = goalCreateReqDto.toEntity(goalCreateReqDto, memberEntity);

        GoalEntity savedGoal = goalRepository.save(goalEntity);
        return GoalResponseDto.fromEntity(savedGoal, memberEntity);
    }

    // 특정 사용자의 모든 목표를 조회
    public List<GoalResponseDto> getAllGoals(UserDetails userDetails) {
        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        return goalRepository.findAllByMemberEntity(memberEntity).stream()
            .map(goalEntity -> GoalResponseDto.fromEntity(goalEntity, memberEntity))
            .collect(Collectors.toList());
    }

    // 진행중인 목표 수정
//    public GoalResponseDto updateGoal(Long id, GoalUpdateReqDto goalUpdateReqDto) {
//        Optional<GoalEntity> optionalGoalEntity = goalRepository.findById(id);
//
//        if (optionalGoalEntity.isEmpty()) {
//            throw new GoalNotFoundException("해당하는 목표를 찾을 수 없습니다.");
//        }
//
//        GoalEntity existingGoalEntity = optionalGoalEntity.get();
//        GoalEntity updatedGoalEntity = goalUpdateReqDto.toEntity(id, existingGoalEntity);
//
//        GoalEntity savedGoal = goalRepository.save(updatedGoalEntity);
//        return GoalResponseDto.fromEntity(savedGoal);
//
//    }
}
