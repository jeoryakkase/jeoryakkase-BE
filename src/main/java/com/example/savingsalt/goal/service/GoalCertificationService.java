package com.example.savingsalt.goal.service;

import com.example.savingsalt.goal.domain.dto.GoalCertificationCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationResponseDto;
import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.exception.GoalNotFoundException;
import com.example.savingsalt.goal.repository.GoalCertificationRepository;
import com.example.savingsalt.goal.repository.GoalRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalCertificationService {

    private final GoalCertificationRepository certificationRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    // 목표 인증 내용 생성
    public GoalCertificationResponseDto createCertification(
        GoalCertificationCreateReqDto goalCertificationCreateReqDto, UserDetails userDetails,
        Long goalId) {
        // 목표와 회원 정보를 가져옴
        GoalEntity goalEntity = goalRepository.findById(goalId)
            .orElseThrow(GoalNotFoundException::new);

        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        // GoalCertificationEntity 생성
        GoalCertificationEntity certificationEntity = goalCertificationCreateReqDto.toEntity(
            goalCertificationCreateReqDto,
            goalEntity,
            memberEntity);

        // 저장
        GoalCertificationEntity savedEntity = certificationRepository.save(certificationEntity);

        return GoalCertificationResponseDto.fromEntity(savedEntity);
    }
}
