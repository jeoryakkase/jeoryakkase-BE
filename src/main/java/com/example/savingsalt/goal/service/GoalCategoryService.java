package com.example.savingsalt.goal.service;

import com.example.savingsalt.goal.domain.dto.GoalCategoryCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCategoryResDto;
import com.example.savingsalt.goal.domain.entity.GoalCategoryEntity;
import com.example.savingsalt.goal.repository.GoalCategoryRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoalCategoryService {

    private final GoalCategoryRepository goalCategoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createGoalCategory(GoalCategoryCreateReqDto goalCategoryCreateReqDto,
        String email) {
        // email 을 이용해 MemberEntity 조회
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("email", email));

        // GoalCategory 엔티티로 변환
        GoalCategoryEntity goalCategory = GoalCategoryEntity.builder()
            .certificationDetails(goalCategoryCreateReqDto.getCertificationDetails())
            .memberEntity(memberEntity)
            .build();

        // 데이터베이스에 저장
        goalCategoryRepository.save(goalCategory);
    }

    @Transactional(readOnly = true)
    public Page<GoalCategoryResDto> getGoalCategoriesByUser(String email, Pageable pageable) {
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new MemberNotFoundException());

        Page<GoalCategoryEntity> goalCategories = goalCategoryRepository.findByMemberEntity(
            memberEntity, pageable);
        return goalCategories.map(GoalCategoryResDto::fromEntity);
    }
}

