package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.global.ChallengeException.ChallengeNotFoundException;
import com.example.savingsalt.global.ChallengeException.MemberChallengeNotFoundException;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    public MemberChallengeServiceImpl(MemberChallengeRepository memberChallengeRepository,
        MemberChallengeMapper memberChallengeMapper
        , MemberRepository memberRepository, ChallengeRepository challengeRepository) {

        this.memberChallengeRepository = memberChallengeRepository;
        this.memberChallengeMapper = memberChallengeMapper;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
    }

    // 회원 챌린지 목록 조회
    public List<MemberChallengeDto> getMemberChallenges(Long memberId) {

        MemberEntity foundMember = memberRepository.findById(memberId).orElseThrow(
            MemberChallengeNotFoundException::new);

        return memberChallengeMapper.toDto(
            memberChallengeRepository.findAllByMemberEntity(foundMember));
    }

    // 회원 챌린지 생성
    public void createMemberChallenge(
        Long ChallengeId, MemberChallengeCreateReqDto memberChallengeCreateReqDto) {
        ChallengeEntity challengeEntity = challengeRepository.findById(ChallengeId).orElseThrow(
            ChallengeNotFoundException::new);

        MemberChallengeEntity memberChallengeEntity = memberChallengeMapper.toEntity(
            memberChallengeCreateReqDto);

        memberChallengeEntity.toBuilder().challengeEntity(challengeEntity);

        memberChallengeRepository.save(memberChallengeEntity);
    }

    // 회원 챌린지 성공
    public void authenticateFinalChallenge(Long memberId, Long memberChallengeId) {
        MemberEntity foundMemberEntity = memberRepository.findById(memberId).orElseThrow(
            MemberChallengeNotFoundException::new);
        MemberChallengeEntity foundMemberChallengeEntity = null;

        List<MemberChallengeEntity> memberChallengeEntities = foundMemberEntity.getMemberChallengeEntities();

        for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
            if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                foundMemberChallengeEntity = memberChallengeEntity;
            }
        }

        // 회원 챌린지 성공 시간 저장
        LocalDateTime currentDate = LocalDateTime.now();

        switch (Objects.requireNonNull(foundMemberChallengeEntity).getChallengeEntity()
            .getChallengeType()) {

            // 챌린지 종류(목표 금액 달성) > 챌린지 목표 금액과 회원 챌린지 목표 달성 금액이 같으면 챌린지 성공
            case "Goal":
                if (Objects.requireNonNull(foundMemberChallengeEntity).getSaveMoney()
                    == foundMemberChallengeEntity.getChallengeEntity().getChallengeGoal()) {

                    Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .certifyDate(currentDate)
                        .build();

                    memberChallengeRepository.save(foundMemberChallengeEntity);
                }
                break;

            // 챌린지 종류(횟수 달성) > 챌린지 목표 횟수와 회원 챌린지 목표 달성 횟수가 같으면 챌린지 성공
            case "Conut":
                if (Objects.requireNonNull(foundMemberChallengeEntity).getChallengeConut()
                    == foundMemberChallengeEntity.getChallengeEntity().getChallengeCount()) {

                    Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                        .challengeStatus(ChallengeStatus.COMPLETED)
                        .certifyDate(currentDate)
                        .build();

                    memberChallengeRepository.save(foundMemberChallengeEntity);
                }
                break;
        }
    }

    // 회원 챌린지 포기
    public void abandonMemberChallenge(Long memberChallengeId) {
        MemberChallengeEntity foundMemberChallengeEntity = memberChallengeRepository.findById(
            memberChallengeId).orElseThrow(
            MemberChallengeNotFoundException::new);

        Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
            .challengeStatus(ChallengeStatus.CANCELLED)
            .build();

        memberChallengeRepository.save(foundMemberChallengeEntity);
    }

    // 회원 챌린지 일일 인증
    public void submitDailyMemberChallenge(Long memberChallengeId,
        MemberChallengeDto memberChallengeDto) {

        MemberChallengeEntity foundMemberChallengeEntity = memberChallengeRepository.findById(
            memberChallengeId).orElseThrow(
            MemberChallengeNotFoundException::new);

        switch (Objects.requireNonNull(foundMemberChallengeEntity).getChallengeEntity()
            .getChallengeType()) {

            // 챌린지 종류(목표 금액 달성) > 기입한 금액만큼 저장
            case "Goal":
                Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                    .saveMoney(foundMemberChallengeEntity.getSaveMoney() + memberChallengeDto.getSaveMoney())
                    .isTodayCertification(true)
                    .build();

                memberChallengeRepository.save(foundMemberChallengeEntity);
                break;

            // 챌린지 종류(횟수 달성) > 기입한 금액만큼 저장 + 달성 횟수 + 1
            case "Conut":
                Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
                    .saveMoney(foundMemberChallengeEntity.getSaveMoney() + memberChallengeDto.getSaveMoney())
                    .challengeConut(foundMemberChallengeEntity.getChallengeConut() + 1)
                    .isTodayCertification(true)
                    .build();

                memberChallengeRepository.save(foundMemberChallengeEntity);
                break;
        }
    }

    // 모든 회원 챌린지 일일 인증 초기화(오전 12시마다)
    public void resetDailyMemberChallengeAuthentication() {
        List<MemberChallengeEntity> memberChallengeEntities = memberChallengeRepository.findAll();

        for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
            memberChallengeEntity.toBuilder()
                .isTodayCertification(false)
                .build();

            memberChallengeRepository.save(memberChallengeEntity);
        }
    }
}
