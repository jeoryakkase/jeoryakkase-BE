package com.example.savingsalt.goal.service;

import com.example.savingsalt.goal.domain.dto.GoalCertificationCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationResponseDto;
import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.goal.exception.GoalNotFoundException;
import com.example.savingsalt.goal.repository.GoalCertificationRepository;
import com.example.savingsalt.goal.repository.GoalRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoalCertificationService {

    private final GoalCertificationRepository certificationRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    // 목표 인증 내용 생성
    @Transactional
    public GoalCertificationResponseDto createCertification(
        GoalCertificationCreateReqDto goalCertificationCreateReqDto, UserDetails userDetails,
        Long goalId) {
        // 목표와 회원 정보를 가져옴
        GoalEntity goalEntity = goalRepository.findById(goalId)
            .orElseThrow(GoalNotFoundException::new);

        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        goalEntity.addCertificationMoney(goalCertificationCreateReqDto.getCertificationMoney());

        // GoalCertificationEntity 생성
        GoalCertificationEntity certificationEntity = goalCertificationCreateReqDto.toEntity(
            goalCertificationCreateReqDto,
            goalEntity,
            memberEntity);

        // 저장
        GoalCertificationEntity savedEntity = certificationRepository.save(certificationEntity);

        // 목표 상태 업데이트
        updateGoalStatus(goalEntity);

        // GoalEntity 업데이트
        goalRepository.save(goalEntity);

        return GoalCertificationResponseDto.fromEntity(savedEntity);
    }

    // 목표 상태를 업데이트하는 메서드
    private void updateGoalStatus(GoalEntity goalEntity) {
        // 현재 금액이 목표 금액을 넘으면 COMPLETE로 설정
        if (goalEntity.getCurrentAmount() >= goalEntity.getGoalAmount()) {
            goalEntity.updateGoalStatus(GoalStatus.COMPLETE);
        }
        goalRepository.save(goalEntity);
    }
}
