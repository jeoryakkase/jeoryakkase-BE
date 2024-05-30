package com.example.savingsalt.challenge.service;

import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.repository.BadgeRepository;
import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.ChallengeMapper;
import com.example.savingsalt.challenge.repository.ChallengeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    public ChallengeServiceImpl(ChallengeRepository challengeRepository,
        ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
    }

    // 챌린지 상세 정보 조회
    @Transactional(readOnly = true)
    public ChallengeDto getChallenge(Long challengeId) {
        ChallengeEntity challengeEntity = challengeRepository.findById(challengeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 챌린지를 찾을 수 없습니다."));
        ChallengeDto challengeDto = challengeMapper.toDto(challengeEntity);
        // Todo: challengeDto가 null이면 예외발생
        return challengeDto;
    }

    // 챌린지 목록 조회
    @Transactional(readOnly = true)
    public Page<ChallengeReadResDto> getAllChallenges(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChallengeEntity> challengeEntities = challengeRepository.findAll(pageable);
        Page<ChallengeReadResDto> challengesReadResDto = challengeMapper.toChallengesReadResDto(
            challengeEntities);

        return challengesReadResDto;
    }

    // 챌린지 키워드 검색
    @Transactional(readOnly = true)
    public Page<ChallengeReadResDto> searchChallengesByKeyword(String keyword, Pageable pageable) {
        Page<ChallengeEntity> challengeEntities;
        if (keyword != null && !keyword.isEmpty()) {
            challengeEntities = challengeRepository.findAllByTitleContaining(keyword, pageable);
        } else {
            challengeEntities = challengeRepository.findAllByOrderByCreatedDateDesc(pageable);
        }

        Page<ChallengeReadResDto> challengesReadResDto = challengeMapper.toChallengesReadResDto(
            challengeEntities);

        return challengesReadResDto;
    }
}
