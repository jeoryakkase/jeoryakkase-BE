package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.global.ChallengeException.ChallengeNotFoundException;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.repository.MemberRepository;
import java.time.LocalDateTime;
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
    private final CertificationChallengeServiceImpl certificationChallengeServiceImpl;

    public MemberChallengeServiceImpl(MemberChallengeRepository memberChallengeRepository,
        MemberChallengeMapper memberChallengeMapper
        , MemberRepository memberRepository, ChallengeRepository challengeRepository,
        CertificationChallengeServiceImpl certificationChallengeServiceImpl) {

        this.memberChallengeRepository = memberChallengeRepository;
        this.memberChallengeMapper = memberChallengeMapper;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
        this.certificationChallengeServiceImpl = certificationChallengeServiceImpl;
    }

    // 회원 챌린지 목록 조회
    public List<MemberChallengeDto> getMemberChallenges(Long memberId) {

        Optional<MemberEntity> MemberEntityOpt = memberRepository.findById(memberId);

        if (MemberEntityOpt.isPresent()) {
            MemberEntity memberEntity = MemberEntityOpt.get();

            return memberChallengeMapper.toDto(
                memberChallengeRepository.findAllByMemberEntity(memberEntity));
        } else {
            throw new MemberNotFoundException();
        }
    }

    // 회원 챌린지 생성
    public MemberChallengeCreateReqDto createMemberChallenge(
        Long memberId, Long ChallengeId, MemberChallengeCreateReqDto memberChallengeCreateReqDto) {

        Optional<ChallengeEntity> challengeEntityOpt = challengeRepository.findById(ChallengeId);
        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (challengeEntityOpt.isPresent()) {
            ChallengeEntity challengeEntity = challengeEntityOpt.get();
            if (memberEntityOpt.isPresent()) {
                MemberEntity memberEntity = memberEntityOpt.get();

                MemberChallengeEntity memberChallengeEntity = memberChallengeMapper.toEntity(
                    memberChallengeCreateReqDto);

                memberChallengeEntity.toBuilder().challengeEntity(challengeEntity)
                    .memberEntity(memberEntity).build();

                return memberChallengeMapper.toMemberChallengeCreateReqDto(
                    memberChallengeRepository.save(memberChallengeEntity));

            } else {
                throw new MemberNotFoundException();
            }
        } else {
            throw new ChallengeNotFoundException();
        }
    }

    // 회원 챌린지 성공
    public void completeMemberChallenge(Long memberId, Long memberChallengeId) {

        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (memberEntityOpt.isPresent()) {
            MemberEntity memberEntity = memberEntityOpt.get();
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();
            LocalDateTime currentDate = LocalDateTime.now();

            ChallengeEntity challengeEntity = null;
            MemberChallengeEntity foundMemberChallengeEntity = null;

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                    foundMemberChallengeEntity = memberChallengeEntity;
                    challengeEntity = foundMemberChallengeEntity.getChallengeEntity();
                }
            }

            // 챌린지 종류(목표 금액 달성) > 챌린지 목표 금액과 회원 챌린지 목표 달성 금액이 같으면 챌린지 성공
            if ("Goal".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                if (Objects.equals(foundMemberChallengeEntity.getTotalSaveMoney(),
                    challengeEntity.getChallengeGoal())) {

                    Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .certifyDate(currentDate)
                        .successConut(foundMemberChallengeEntity.getSuccessConut() + 1)
                        .build();

                    memberChallengeRepository.save(foundMemberChallengeEntity);
                }
            }
            // 챌린지 종류(횟수 달성) > 챌린지 목표 횟수와 회원 챌린지 목표 달성 횟수가 같으면 챌린지 성공
            else if ("Conut".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                if (Objects.equals(foundMemberChallengeEntity.getChallengeConut(),
                    challengeEntity.getChallengeCount())) {

                    Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .successConut(foundMemberChallengeEntity.getSuccessConut() + 1)
                        .certifyDate(currentDate)
                        .build();

                    memberChallengeRepository.save(foundMemberChallengeEntity);
                }
            }
        } else {
            throw new MemberNotFoundException();
        }
    }

    // 회원 챌린지 포기
    public void abandonMemberChallenge(Long memberId, Long memberChallengeId) {
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

            Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                .challengeStatus(ChallengeStatus.CANCELLED)
                .build();

            memberChallengeRepository.save(foundMemberChallengeEntity);
        } else {
            throw new MemberNotFoundException();
        }
    }

    // 회원 챌린지 일일 인증
    public void certifyDailyMemberChallenge(Long memberId, Long memberChallengeId,
        CertificationChallengeReqDto certificationChallengeReqDto) {

        Optional<MemberEntity> memberEntityOpt = memberRepository.findById(memberId);

        if (memberEntityOpt.isPresent()) {
            MemberEntity memberEntity = memberEntityOpt.get();
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();

            MemberChallengeEntity foundMemberChallengeEntity = null;
            ChallengeEntity challengeEntity = null;

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                    foundMemberChallengeEntity = memberChallengeEntity;
                    challengeEntity = memberChallengeEntity.getChallengeEntity();
                }
            }

            certificationChallengeServiceImpl.createCertificationChallenge(
                foundMemberChallengeEntity,
                certificationChallengeReqDto);

            // 총 절약 금액 저장 + 일일 인증 상태 True
            if ("Goal".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                    .totalSaveMoney(foundMemberChallengeEntity.getTotalSaveMoney()
                        + certificationChallengeReqDto.getSaveMoney())
                    .isTodayCertification(true)
                    .build();

                memberChallengeRepository.save(foundMemberChallengeEntity);
            }
            // 총 절약 금액 저장 + 일일 인증 상태 True + 달성 횟수 1 증가
            else if ("Count".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                    .totalSaveMoney(
                        foundMemberChallengeEntity.getTotalSaveMoney()
                            + certificationChallengeReqDto.getSaveMoney())
                    .challengeConut(foundMemberChallengeEntity.getChallengeConut() + 1)
                    .isTodayCertification(true)
                    .build();

                memberChallengeRepository.save(foundMemberChallengeEntity);
            }
        }
    }

    // 모든 회원 챌린지 일일 인증 초기화(오전 12시마다)
    public void resetDailyMemberChallengeAuthentication() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                memberChallengeEntity.toBuilder()
                    .isTodayCertification(false)
                    .build();

                memberChallengeRepository.save(memberChallengeEntity);
            }
        }
    }
}
