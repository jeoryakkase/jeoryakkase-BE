package com.example.savingsalt.challenge.service;

import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.repository.BadgeRepository;
import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeDifficulty;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.exception.ChallengeException.InvalidChallengeGoalAndCountException;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.ChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.badge.exception.BadgeException.BadgeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.ChallengeNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;
    private final BadgeRepository badgeRepository;
    private final MemberChallengeRepository memberChallengeRepository;

    public ChallengeServiceImpl(ChallengeRepository challengeRepository,
        ChallengeMapper challengeMapper, BadgeRepository badgeRepository,
        MemberChallengeRepository memberChallengeRepository) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
        this.badgeRepository = badgeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
    }

    // 챌린지 상세 정보 조회
    @Transactional(readOnly = true)
    public ChallengeDto getChallenge(Long challengeId) {
        ChallengeEntity challengeEntity = challengeRepository.findById(challengeId)
            .orElseThrow(ChallengeNotFoundException::new);
        ChallengeDto challengeDto = challengeMapper.toDto(challengeEntity);

        return challengeDto;
    }

    // 챌린지 목록 조회
    @Transactional(readOnly = true)
    public Page<ChallengeReadResDto> getAllChallenges(Pageable pageable) {
        Page<ChallengeEntity> challengeEntities = challengeRepository.findAll(pageable);

        Page<ChallengeReadResDto> challengesReadResDto = challengeEntities.map(
            challengeMapper::toChallengesReadResDto);

        return challengesReadResDto;
    }

    // 챌린지 키워드 검색
    @Transactional(readOnly = true)
    public Page<ChallengeReadResDto> searchChallengesByKeyword(String keyword, Pageable pageable) {
        Page<ChallengeEntity> challengeEntities;
        if (keyword != null && !keyword.isEmpty()) {
            challengeEntities = challengeRepository.findAllByChallengeTypeContaining(keyword,
                pageable);
        } else {
            challengeEntities = challengeRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        Page<ChallengeReadResDto> challengesReadResDto = challengeEntities.map(
            challengeMapper::toChallengesReadResDto);

        return challengesReadResDto;
    }

    // 챌린지 생성
    public ChallengeDto createChallenge(ChallengeCreateReqDto challengeCreateDto) {
        BadgeEntity badgeEntity = badgeRepository.findById(challengeCreateDto.getBadgeId())
            .orElseThrow(BadgeNotFoundException::new);

        // 챌린지 생성시 챌린지 목표 금액, 챌린지 목표 횟수가 하나만 입력 됐는지 검증
        int challengeGoal = challengeCreateDto.getChallengeGoal();
        int challengeCount = challengeCreateDto.getChallengeCount();
        if ((challengeGoal == 0 && challengeCount == 0) || (challengeGoal != 0
            && challengeCount != 0)) {
            throw new InvalidChallengeGoalAndCountException();
        }

        ChallengeEntity challengeEntity = challengeMapper.toEntity(challengeCreateDto);
        ChallengeEntity challengeAndBadgeEntity = challengeEntity.toBuilder()
            .badgeEntity(badgeEntity)
            .build();

        ChallengeEntity createdChallengeEntity = challengeRepository.save(challengeAndBadgeEntity);
        ChallengeDto createdChallengeDto = challengeMapper.toDto(createdChallengeEntity);

        return createdChallengeDto;
    }

    // 챌린지 수정
    public ChallengeDto updateChallenge(Long challengeId,
        ChallengeUpdateReqDto updatedChallengeDto) {
        ChallengeEntity challengeEntity = challengeRepository.findById(challengeId)
            .orElseThrow(ChallengeNotFoundException::new);

        BadgeEntity badgeEntity = badgeRepository.findById(updatedChallengeDto.getBadgeId())
            .orElseThrow(BadgeNotFoundException::new);

        ChallengeEntity updateChallengeEntity = challengeEntity.toBuilder()
            .challengeTitle(updatedChallengeDto.getChallengeTitle())
            .challengeDesc(updatedChallengeDto.getChallengeDesc())
            .challengeGoal(updatedChallengeDto.getChallengeGoal())
            .challengeCount(updatedChallengeDto.getChallengeCount())
            .challengeType(updatedChallengeDto.getChallengeType())
            .challengeTerm(updatedChallengeDto.getChallengeTerm())
            .challengeDifficulty(updatedChallengeDto.getChallengeDifficulty())
            .authContent(updatedChallengeDto.getAuthContent())
            .badgeEntity(badgeEntity)
            .build();

        ChallengeEntity updatedChallengeEntity = challengeRepository.save(updateChallengeEntity);
        ChallengeDto challengeDto = challengeMapper.toDto(updatedChallengeEntity);

        return challengeDto;
    }

    // 챌린지 난이도 설정(챌린지별 달성률 기준 30%: HARD(상), 60%: NORMAL(중), 나머지: EASY(하))
    public void setChallengeDifficulty(Long challengeId) {
        ChallengeEntity challengeEntity = challengeRepository.findById(challengeId)
            .orElseThrow(ChallengeNotFoundException::new);
        List<MemberChallengeEntity> memberChallengeEntity = memberChallengeRepository.findAllByChallengeEntity(
            challengeEntity);

        // 전체 회원들이 챌린지에 시도한 수
        long totalMemberChallengeSize = memberChallengeEntity.size();
        if (totalMemberChallengeSize == 0) {
            return;
        }

        // 회원 챌린지에서 각각 처음 완료한 챌린지의 수
        long successMemberChallengeSize = 0;

        for (int i = 0; i < memberChallengeEntity.size(); i++) {
            if ((memberChallengeEntity.get(i).getChallengeStatus()
                == ChallengeStatus.COMPLETED) && (
                memberChallengeEntity.get(i).getSuccessConut() == 0)) {
                successMemberChallengeSize++;
            }
        }

        // 챈린지 난이도 계산
        ChallengeDifficulty difficulty;
        double successPercent =
            (double) totalMemberChallengeSize / (double) successMemberChallengeSize * 100;
        if (successPercent <= 30) {
            difficulty = ChallengeEntity.ChallengeDifficulty.HARD;
        } else if (successPercent <= 60) {
            difficulty = ChallengeEntity.ChallengeDifficulty.NORMAL;
        } else {
            difficulty = ChallengeEntity.ChallengeDifficulty.EASY;
        }

        // 계산한 난이도 해당 챌린지에 적용
        ChallengeEntity updatedChallengeEntity = challengeEntity.toBuilder()
            .challengeDifficulty(difficulty)
            .build();
        challengeRepository.save(updatedChallengeEntity);
    }

    // 챌린지 삭제
    public void deleteChallenge(Long challengeId) {
        challengeRepository.findById(challengeId)
            .orElseThrow(ChallengeNotFoundException::new);

        challengeRepository.deleteById(challengeId);
    }

}
