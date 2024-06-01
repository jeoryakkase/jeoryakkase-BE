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
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.ChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.global.ChallengeException.BadgeNotFoundException;
import com.example.savingsalt.global.ChallengeException.ChallengeNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
        // Todo: challengeDto가 null이면 예외발생
        return challengeDto;
    }

    // 챌린지 목록 조회
    @Transactional(readOnly = true)
    public Page<ChallengeReadResDto> getAllChallenges(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
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
            challengeEntities = challengeRepository.findAllByChallengeTitleContaining(keyword,
                pageable);
        } else {
            challengeEntities = challengeRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        Page<ChallengeReadResDto> challengesReadResDto = challengeEntities.map(
            challengeMapper::toChallengesReadResDto);

        return challengesReadResDto;
    }

    // 챌린지 생성
    @Transactional
    public ChallengeDto createChallenge(ChallengeCreateReqDto challengeCreateDto) {
        ChallengeEntity challengeEntity = challengeMapper.toEntity(challengeCreateDto);
        ChallengeEntity createdChallengeEntity = challengeRepository.save(challengeEntity);
        ChallengeDto createdChallengeDto = challengeMapper.toDto(createdChallengeEntity);
        // Todo: challengeEntity, createdChallengeEntity, createdChallengeDto가 null이면 예외발생 ("챌린지 정보를 저장하는데 실패했습니다.");
        return createdChallengeDto;
    }

    // 챌린지 수정
    @Transactional
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
        // Todo: updatedChallengeEntity, challengeDto가 null이면 예외발생 ("챌린지 정보를 수정하는데 실패했습니다.");
        return challengeDto;
    }

    // 챌린지 난이도 설정(챌린지별 달성률 기준 30%: 상, 60%: 중, 나머지: 하)
    @Transactional
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

        //회원 챌린지에서 각각 처음 완료한 챌린지의 수
        long successMemberChallengeSize = 0;

        for (int i = 0; i < memberChallengeEntity.size(); i++) {
            if ((memberChallengeEntity.get(i).getChallengeStatus()
                == ChallengeStatus.COMPLETED) && (
                memberChallengeEntity.get(i).getChallengeTry() == 0)) {
                successMemberChallengeSize++;
            }
        }

        ChallengeEntity updatedChallengeEntity;
        double successPercent =
            (double) totalMemberChallengeSize / (double) successMemberChallengeSize * 100;
        if (successPercent <= 30) {
            updatedChallengeEntity = challengeEntity.toBuilder()
                .challengeDifficulty(ChallengeDifficulty.HARD)
                .build();
        } else if (successPercent <= 60) {
            updatedChallengeEntity = challengeEntity.toBuilder()
                .challengeDifficulty(ChallengeDifficulty.NORMAL)
                .build();
        } else {
            updatedChallengeEntity = challengeEntity.toBuilder()
                .challengeDifficulty(ChallengeDifficulty.EASY)
                .build();
        }

        challengeRepository.save(updatedChallengeEntity);
    }

    // 챌린지 삭제
    @Transactional
    public void deleteChallenge(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }

}
