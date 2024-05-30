package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.community.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {

    // 검색 키워드에 맞는 모든 챌린지 객체를 Page로 넘겨주기
    Page<ChallengeEntity> findAllByTitleContaining(String keyword, Pageable pageable);

    // Page로 생성일자 내림차순으로 조회된 모든 챌린지 객체들을 넘겨주기
    Page<ChallengeEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);

}
