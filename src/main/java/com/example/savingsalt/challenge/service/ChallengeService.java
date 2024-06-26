package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import org.springframework.data.domain.Page;

public interface ChallengeService {

    // 챌린지 상세 정보 조회
    ChallengeDto getChallenge(Long challengeId);

    // 챌린지 목록 조회(인기 챌린지 목록 조회 포함)
    Page<ChallengeReadResDto> getAllChallenges(String keyword, int page, int size);

    // 챌린지 생성
    ChallengeDto createChallenge(ChallengeCreateReqDto challengeCreateDto);

    // 챌린지 수정(제목으로 챌린지 구분)
    ChallengeDto updateChallenge(Long challengeId, ChallengeUpdateReqDto updatedChallengeDto);

    // 챌린지 난이도 설정
    void setChallengeDifficulty(Long challengeId);

    // 챌린지 삭제
    void deleteChallenge(Long challengeId);

}
