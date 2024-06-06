package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCompleteReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.exception.ChallengeException.ChallengeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.MemberChallengeFailureException;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeMapper;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeWithCertifyAndChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
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
    private final MemberChallengeWithCertifyAndChallengeMapper memberChallengeWithCertifyAndChallengeMapper;

    public MemberChallengeServiceImpl(MemberChallengeRepository memberChallengeRepository,
        MemberChallengeMapper memberChallengeMapper
        , MemberRepository memberRepository, ChallengeRepository challengeRepository,
        CertificationChallengeServiceImpl certificationChallengeServiceImpl,
        MemberChallengeWithCertifyAndChallengeMapper memberChallengeWithCertifyAndChallengeMapper) {

        this.memberChallengeRepository = memberChallengeRepository;
        this.memberChallengeMapper = memberChallengeMapper;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
        this.certificationChallengeServiceImpl = certificationChallengeServiceImpl;
        this.memberChallengeWithCertifyAndChallengeMapper = memberChallengeWithCertifyAndChallengeMapper;
    }

    // 회원 챌린지 목록 조회
    public List<MemberChallengeWithCertifyAndChallengeResDto> getMemberChallenges(Long memberId) {

        Optional<MemberEntity> MemberEntityOpt = memberRepository.findById(memberId);

        if (MemberEntityOpt.isPresent()) {
            MemberEntity memberEntity = MemberEntityOpt.get();

            return memberChallengeWithCertifyAndChallengeMapper.toDto(
                memberChallengeRepository.findAllWithFetchJoinByMemberEntity(memberEntity));
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

                memberChallengeEntity = memberChallengeEntity.toBuilder()
                    .challengeEntity(challengeEntity)
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
    public MemberChallengeCompleteReqDto completeMemberChallenge(Long memberId,
        Long memberChallengeId) {

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

            // 챌린지 종류(횟수 달성) > 챌린지 목표 횟수와 회원 챌린지 목표 달성 횟수가 같으면 챌린지 성공
            if ("Count".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                if (Objects.equals(foundMemberChallengeEntity.getChallengeConut(),
                    challengeEntity.getChallengeCount())) {

                    foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                        .toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .successConut(foundMemberChallengeEntity.getSuccessConut() + 1)
                        .certifyDate(currentDate)
                        .build();

                    return memberChallengeMapper.toMemberChallengeCompleteReqDto(
                        memberChallengeRepository.save(foundMemberChallengeEntity));
                } else {
                    throw new MemberChallengeFailureException();
                }
            }
            // 챌린지 종류(목표 금액 달성) > 챌린지 목표 금액과 회원 챌린지 목표 달성 금액이 같으면 챌린지 성공
            else if ("Goal".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                if (foundMemberChallengeEntity.getTotalSaveMoney()
                    >= challengeEntity.getChallengeGoal()) {

                    foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                        .toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .certifyDate(currentDate)
                        .successConut(foundMemberChallengeEntity.getSuccessConut() + 1)
                        .build();

                    return memberChallengeMapper.toMemberChallengeCompleteReqDto(
                        memberChallengeRepository.save(foundMemberChallengeEntity));
                } else {
                    throw new MemberChallengeFailureException();
                }
            }
        } else {
            throw new MemberNotFoundException();
        }

        return null;
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

            foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                .toBuilder()
                .challengeStatus(ChallengeStatus.CANCELLED)
                .build();

            memberChallengeRepository.save(foundMemberChallengeEntity);
        } else {
            throw new MemberNotFoundException();
        }
    }

    // 회원 챌린지 일일 인증
    public MemberChallengeDto certifyDailyMemberChallenge(Long memberId, Long memberChallengeId,
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

            // 일일 인증 상태 True + 총 절약 금액 저장
            if ("Goal".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                    .toBuilder()
                    .totalSaveMoney(foundMemberChallengeEntity.getTotalSaveMoney()
                        + certificationChallengeReqDto.getSaveMoney())
                    .isTodayCertification(true)
                    .build();

                return memberChallengeMapper.toDto(
                    memberChallengeRepository.save(foundMemberChallengeEntity));
            }
            // 일일 인증 상태 True + 달성 횟수 1 증가
            else if ("Count".equals(Objects.requireNonNull(challengeEntity).getChallengeType())) {
                foundMemberChallengeEntity = Objects.requireNonNull(foundMemberChallengeEntity)
                    .toBuilder()
                    .challengeConut(foundMemberChallengeEntity.getChallengeConut() + 1)
                    .isTodayCertification(true)
                    .build();

                return memberChallengeMapper.toDto(
                    memberChallengeRepository.save(foundMemberChallengeEntity));
            }
        }
        return null;
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

}
