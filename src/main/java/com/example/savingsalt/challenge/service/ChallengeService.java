package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.ChallengeDto;
import com.example.savingsalt.challenge.domain.ChallengeUpdateReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeService {

    // 챌린지 목록 아이디 조회
    ChallengeDto getChallenge(Long challengeId);

    // 챌린지 목록 조회
    Page<ChallengeDto> getAllChallenges(int page, int size);

    // 챌린지 키워드 검색
    Page<ChallengeDto> searchChallengesByKeyword(String keyword, Pageable pageable);

    // 챌린지 생성
    ChallengeDto createChallenge(ChallengeCreateReqDto challengeCreateDto);

    // 챌린지 수정(제목으로 챌린지 구분)
    ChallengeDto updateChallenge(ChallengeUpdateReqDto updatedChallengeDto);

    // 챌린지 난이도 설정
    void setChallengeDifficulty(Long challengeId);

    // 챌린지 삭제
    void deleteChallenge(Long challengeId);

}
