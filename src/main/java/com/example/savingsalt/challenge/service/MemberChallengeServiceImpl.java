package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeAbandonResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeJoinResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.exception.ChallengeException.CertificationChallengeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.ChallengeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.InvalidChallengeTermException;
import com.example.savingsalt.challenge.exception.ChallengeException.MemberChallengeAlreadySucceededException;
import com.example.savingsalt.challenge.exception.ChallengeException.MemberChallengeNotFoundException;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeMapper;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeWithCertifyAndChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MemberChallengeServiceImpl implements
    MemberChallengeService {

    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberChallengeMapper memberChallengeMapper;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final CertificationChallengeServiceImpl certificationChallengeService;
    private final MemberChallengeWithCertifyAndChallengeMapper memberChallengeWithCertifyAndChallengeMapper;
    private final ChallengeServiceImpl challengeService;
    private final EntityManager entityManager;

    public MemberChallengeServiceImpl(
        MemberChallengeRepository memberChallengeRepository,
        MemberChallengeMapper memberChallengeMapper,
        MemberRepository memberRepository,
        ChallengeRepository challengeRepository,
        CertificationChallengeServiceImpl certificationChallengeService,
        MemberChallengeWithCertifyAndChallengeMapper memberChallengeWithCertifyAndChallengeMapper,
        ChallengeServiceImpl challengeService,
        EntityManager entityManager) {

        this.memberChallengeRepository = memberChallengeRepository;
        this.memberChallengeMapper = memberChallengeMapper;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
        this.memberChallengeWithCertifyAndChallengeMapper = memberChallengeWithCertifyAndChallengeMapper;
        this.challengeService = challengeService;
        this.certificationChallengeService = certificationChallengeService;
        this.entityManager = entityManager;
    }

    // 회원 챌린지 목록 조회
    public List<MemberChallengeWithCertifyAndChallengeResDto> getMemberChallenges(Long memberId) {

        Optional<MemberEntity> MemberEntityOpt = memberRepository.findById(memberId);

        if (MemberEntityOpt.isPresent()) {
            MemberEntity memberEntity = MemberEntityOpt.get();

            return memberChallengeWithCertifyAndChallengeMapper.toDtoList(
                memberChallengeRepository.findAllByMemberEntity(memberEntity));
        } else {
            throw new MemberNotFoundException();
        }
    }

    // 회원 챌린지 생성
    public MemberChallengeCreateResDto createMemberChallenge(
        Long memberId, Long ChallengeId) {

        Optional<ChallengeEntity> challengeEntityOpt = challengeRepository.findById(ChallengeId);
        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (challengeEntityOpt.isPresent()) {
            ChallengeEntity challengeEntity = challengeEntityOpt.get();

            if (memberEntityOpt.isPresent()) {
                MemberEntity memberEntity = memberEntityOpt.get();

                LocalDateTime startDate = LocalDateTime.now();
                LocalDateTime endDate = getLocalEndDateTime(challengeEntity, startDate);

                MemberChallengeEntity memberChallengeEntity = MemberChallengeEntity.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .successDate(null)
                    .challengeEntity(challengeEntity)
                    .authCount(0)
                    .successCount(0)
                    .totalSaveMoney(0)
                    .isTodayCertification(false)
                    .challengeComment("")
                    .challengeStatus(ChallengeStatus.IN_PROGRESS)
                    .memberEntity(memberEntity).build();

                return memberChallengeMapper.toMemberChallengeCreateResDto(
                    memberChallengeRepository.save(memberChallengeEntity));

            } else {
                throw new MemberNotFoundException();
            }
        } else {
            throw new ChallengeNotFoundException();
        }
    }

    // 회원 챌린지 포기
    public MemberChallengeAbandonResDto abandonMemberChallenge(Long memberId,
        Long memberChallengeId) {
        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (memberEntityOpt.isPresent()) {
            MemberEntity memberEntity = memberEntityOpt.get();
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();

            MemberChallengeEntity foundMemberChallengeEntity = null;

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                    foundMemberChallengeEntity = memberChallengeEntity;
                }
            }

            if (Objects.requireNonNull(foundMemberChallengeEntity).getChallengeStatus()
                .equals(ChallengeStatus.COMPLETED)) {
                throw new MemberChallengeAlreadySucceededException();
            } else {
                foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                    .toBuilder()
                    .challengeStatus(ChallengeStatus.CANCELLED)
                    .build();

                return memberChallengeMapper.toMemberChallengeAbandonResDto(
                    memberChallengeRepository.save(foundMemberChallengeEntity));
            }

        } else {
            throw new MemberNotFoundException();
        }
    }

    // 회원 챌린지 인증
    public MemberChallengeDto certifyDailyMemberChallenge(Long memberId, Long memberChallengeId,
        CertificationChallengeReqDto certificationChallengeReqDto, List<String> imageUrls) {

        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (memberEntityOpt.isPresent()) {
            MemberEntity memberEntity = memberEntityOpt.get();
            LocalDateTime currentDate = LocalDateTime.now();
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();

            MemberChallengeEntity foundMemberChallengeEntity = null;
            ChallengeEntity challengeEntity = null;

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                    foundMemberChallengeEntity = memberChallengeEntity;
                    challengeEntity = memberChallengeEntity.getChallengeEntity();
                }
            }

            if (Objects.requireNonNull(foundMemberChallengeEntity).getChallengeStatus()
                .equals(ChallengeStatus.COMPLETED)) {
                throw new MemberChallengeAlreadySucceededException();
            }

            // 챌린지 인증 DTO -> 챌린지 일일 인증 DB로 저장
            CertificationChallengeDto certificationChallengeDto = certificationChallengeService.createCertificationChallenge(
                foundMemberChallengeEntity, certificationChallengeReqDto, imageUrls);

            // 챌린지 종류 'Goal' > 금액 달성 방식
            if ("Goal".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {

                foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                    .toBuilder()
                    .totalSaveMoney(foundMemberChallengeEntity.getTotalSaveMoney()
                        + certificationChallengeReqDto.getSaveMoney())
                    .isTodayCertification(true)
                    .build();

                // 챌린지 성공 확인 (절약한 금액이 챌린지 목표 금액을 넘으면 성공)
                if (challengeEntity.getChallengeGoal()
                    <= foundMemberChallengeEntity.getTotalSaveMoney()) {

                    foundMemberChallengeEntity = foundMemberChallengeEntity
                        .toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .successDate(currentDate)
                        .successCount(foundMemberChallengeEntity.getSuccessCount() + 1)
                        .build();

                    challengeService.setChallengeDifficulty(challengeEntity.getId());
                }

                MemberChallengeDto foundMemberChallengeDto = memberChallengeMapper.toDto(
                    memberChallengeRepository.save(foundMemberChallengeEntity));

                foundMemberChallengeDto = foundMemberChallengeDto.toBuilder()
                    .certificationChallengeDto(certificationChallengeDto).build();

                return foundMemberChallengeDto;

            }
            // 챌린지 종류 'Count' > 목표 달성 방식
            else if ("Count".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {

                foundMemberChallengeEntity = foundMemberChallengeEntity
                    .toBuilder()
                    .authCount(foundMemberChallengeEntity.getAuthCount() + 1)
                    .isTodayCertification(true)
                    .build();

                // 챌린지 성공 확인 (목표 횟수가 챌린지 목표 횟수를 넘으면 성공)
                if (challengeEntity.getChallengeCount()
                    <= foundMemberChallengeEntity.getAuthCount()) {

                    foundMemberChallengeEntity = foundMemberChallengeEntity
                        .toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .successCount(foundMemberChallengeEntity.getSuccessCount() + 1)
                        .successDate(currentDate)
                        .build();

                    challengeService.setChallengeDifficulty(challengeEntity.getId());
                }

                MemberChallengeDto foundMemberChallengeDto = memberChallengeMapper.toDto(
                    memberChallengeRepository.save(foundMemberChallengeEntity));

                foundMemberChallengeDto = foundMemberChallengeDto.toBuilder()
                    .certificationChallengeDto(certificationChallengeDto).build();

                return foundMemberChallengeDto;
            }
        } else {
            throw new MemberNotFoundException();
        }
        return null;
    }

    // 참여 중인 챌린지 목록 조회
    public List<MemberChallengeJoinResDto> getJoiningMemberChallenge(Long memberId) {
        Optional<MemberEntity> MemberEntityOpt = memberRepository.findById(memberId);
        List<MemberChallengeEntity> memberChallengeEntities;
        List<MemberChallengeJoinResDto> memberChallengeJoinResDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (MemberEntityOpt.isPresent()) {
            MemberEntity memberEntity = MemberEntityOpt.get();
            memberChallengeEntities = memberChallengeRepository.findAllByMemberEntity(memberEntity);
            if (memberChallengeEntities.isEmpty()) {
                throw new MemberChallengeNotFoundException();
            } else {
                for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                    if (memberChallengeEntity.getChallengeStatus()
                        .equals(ChallengeStatus.IN_PROGRESS)) {

                        Long effectiveDate = ChronoUnit.DAYS.between(now,
                            memberChallengeEntity.getStartDate());

                        MemberChallengeJoinResDto tempMemberChallengeJoinResDto = MemberChallengeJoinResDto.builder()
                            .challengeTtile(
                                memberChallengeEntity.getChallengeEntity().getChallengeTitle())
                            .challengeTerm(
                                memberChallengeEntity.getChallengeEntity().getChallengeTerm())
                            .isTodayCertification(memberChallengeEntity.getIsTodayCertification())
                            .startDate(memberChallengeEntity.getStartDate().toLocalDate())
                            .endDate(memberChallengeEntity.getEndDate().toLocalDate())
                            .effectiveDate(effectiveDate)
                            .build();

                        memberChallengeJoinResDtoList.add(tempMemberChallengeJoinResDto);
                    }

                }
                return memberChallengeJoinResDtoList;
            }

        } else {
            throw new MemberNotFoundException();
        }
    }

    private static LocalDateTime getLocalEndDateTime(ChallengeEntity challengeEntity,
        LocalDateTime startDate) {

        return switch (challengeEntity
            .getChallengeTerm()) {
            case "1일" -> startDate.plusDays(1);
            case "3일" -> startDate.plusDays(3);
            case "5일" -> startDate.plusDays(5);
            case "1주" -> startDate.plusDays(7);
            case "2주" -> startDate.plusDays(14);
            case "3주" -> startDate.plusDays(21);
            case "한 달" -> startDate.plusDays(30);
            default -> throw new InvalidChallengeTermException();
        };
    }

    // 모든 회원 챌린지 일일 인증 초기화(오전 12시마다)
    public void resetDailyMemberChallengeAuthentication() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                memberChallengeEntity = memberChallengeEntity.toBuilder()
                    .isTodayCertification(false)
                    .build();

                memberChallengeRepository.save(memberChallengeEntity);
            }
        }
    }

    // TODO: 영속성 컨텍스트 문제로 컬럼 삭제가 이루어지지 않는 문제 해결
    // 챌린지 인증 삭제
    public void deleteCertificationChallenge(Long memberId, Long memberChallengeId,
        Long certificationId) {
        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (memberEntityOpt.isPresent()) {
            MemberEntity memberEntity = memberEntityOpt.get();
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();
            MemberChallengeEntity foundMemberChallengeEntity = null;

            List<CertificationChallengeEntity> certificationChallengeEntities;

            if (memberChallengeEntities.isEmpty()) {
                throw new MemberChallengeNotFoundException();
            } else {
                for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                    if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                        foundMemberChallengeEntity = memberChallengeEntity;
                    }
                }

                certificationChallengeEntities = Objects.requireNonNull(foundMemberChallengeEntity)
                    .getCertificationChallengeEntities();

                for (CertificationChallengeEntity certificationChallengeEntity : certificationChallengeEntities) {
                    if (Objects.equals(certificationChallengeEntity.getId(), certificationId)) {

                        certificationChallengeService.deleteCertificationChallengeById(
                            certificationChallengeEntity.getId());

                        return;
                    }
                }
                throw new CertificationChallengeNotFoundException();
            }
        }
    }
}
