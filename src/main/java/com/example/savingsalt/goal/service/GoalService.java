package com.example.savingsalt.goal.service;

import com.example.savingsalt.config.s3.S3Service;
import com.example.savingsalt.goal.domain.dto.GoalCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.goal.exception.GoalNotFoundException;
import com.example.savingsalt.goal.exception.MaxProceedingGoalsExceededException;
import com.example.savingsalt.goal.exception.PermissionDeniedException;
import com.example.savingsalt.goal.repository.GoalRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;  // S3Service 의존성 추가

    // 목표를 리포지토리에 저장
    @Transactional
    public GoalResponseDto createGoal(GoalCreateReqDto goalCreateReqDto, MultipartFile image,
        UserDetails userDetails) {
        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        // 진행중인 목표의 개수를 확인
        long proceedingGoalsCount = goalRepository.countByMemberEntityAndGoalStatus(memberEntity,
            GoalStatus.PROCEEDING);

        if (proceedingGoalsCount >= 5) {
            throw new MaxProceedingGoalsExceededException();
        }

        String imageUrl = null;
        try {
            imageUrl = s3Service.upload(image);  // 이미지 업로드 및 URL 반환
        } catch (IOException e) {
        }

        goalCreateReqDto.setGoalImage(imageUrl);  // 이미지 URL을 DTO에 설정
        GoalEntity goalEntity = goalCreateReqDto.toEntity(memberEntity);
        GoalEntity savedGoal = goalRepository.save(goalEntity);

        return GoalResponseDto.fromEntity(savedGoal);
    }

    // 특정 사용자의 모든 목표를 조회
    @Transactional(readOnly = true)  // 조회 전용 트랜잭션으로 변경
    public Page<GoalResponseDto> getAllGoals(UserDetails userDetails, Pageable pageable) {
        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        Page<GoalEntity> goalEntities = goalRepository.findAllByMemberEntity(memberEntity,
            pageable);
        return goalEntities.map(GoalResponseDto::fromEntity);
    }

    // 진행중인 목표 포기
    @Transactional
    public GoalResponseDto giveUpGoal(Long id, UserDetails userDetails) {
        MemberEntity memberEntity = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(MemberNotFoundException::new);

        GoalEntity goalEntity = goalRepository.findById(id)
            .orElseThrow(GoalNotFoundException::new);

        // 사용자가 목표의 소유자인지 확인
        if (!goalEntity.getMemberEntity().getId().equals(memberEntity.getId())) {
            throw new PermissionDeniedException();
        }

        goalEntity.updateGoalStatus(GoalStatus.GIVE_UP);
        goalRepository.save(goalEntity);

        return GoalResponseDto.fromEntity(goalEntity);
    }
}
