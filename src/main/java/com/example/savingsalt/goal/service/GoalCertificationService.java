package com.example.savingsalt.goal.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.savingsalt.goal.domain.dto.GoalCertificationCreateReqDto;
import com.example.savingsalt.goal.domain.dto.GoalCertificationResponseDto;
import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.goal.exception.CertificationNotFoundException;
import com.example.savingsalt.goal.exception.GoalNotFoundException;
import com.example.savingsalt.goal.repository.GoalCertificationRepository;
import com.example.savingsalt.goal.repository.GoalRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoalCertificationService {

    private final GoalCertificationRepository certificationRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3 amazonS3Client;

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

    // 목표 인증 삭제
    @Transactional
    public void deleteCertification(Long goalId, Long certificationId, UserDetails userDetails) {
        // 목표와 회원 정보를 가져옴
        GoalEntity goalEntity = goalRepository.findById(goalId)
            .orElseThrow(GoalNotFoundException::new);

        // 인증 엔티티를 조회
        GoalCertificationEntity certificationEntity = certificationRepository.findById(
                certificationId)
            .orElseThrow(CertificationNotFoundException::new);

        // 현재 인증 금액만큼 목표 금액에서 차감
        goalEntity.subtractCertificationMoney(certificationEntity.getCertificationMoney());

        // 이미지 URL을 가져와서 S3에서 삭제
        String imageUrl = certificationEntity.getCertificationImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            deleteImageFromS3(imageUrl);
        }

        // 인증 삭제
        certificationRepository.delete(certificationEntity);

        // 목표 상태 업데이트
        updateGoalStatus(goalEntity);

        // GoalEntity 업데이트
        goalRepository.save(goalEntity);
    }

    // 특정 목표의 모든 인증을 최신순으로 조회
    @Transactional(readOnly = true)
    public Page<GoalCertificationResponseDto> getCertificationsByGoal(Long goalId,
        UserDetails userDetails, Pageable pageable) {
        GoalEntity goalEntity = goalRepository.findById(goalId)
            .orElseThrow(() -> new GoalNotFoundException());

        Page<GoalCertificationEntity> certifications = certificationRepository.findAllByGoalEntityOrderByCertificationDateDesc(
            goalEntity, pageable);

        return certifications.map(GoalCertificationResponseDto::fromEntity);
    }

    // 목표 상태를 업데이트하는 메서드
    private void updateGoalStatus(GoalEntity goalEntity) {
        // 현재 금액이 목표 금액을 넘으면 COMPLETE로 설정
        if (goalEntity.getCurrentAmount() >= goalEntity.getGoalAmount()) {
            goalEntity.updateGoalStatus(GoalStatus.COMPLETE);
        }
        goalRepository.save(goalEntity);
    }

    // S3에서 이미지 삭제하는 메서드
    private void deleteImageFromS3(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        amazonS3Client.deleteObject("my.eliceproject.s3.bucket", fileName);
    }
}
