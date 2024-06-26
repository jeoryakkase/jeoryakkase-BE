package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {

    // 검색 키워드에 맞는 모든 챌린지 객체를 Page로 넘겨주기
    Page<ChallengeEntity> findAllByChallengeTypeContaining(String keyword, Pageable pageable);

    // Page로 생성일자 내림차순으로 조회된 모든 챌린지 객체들을 넘겨주기
    Page<ChallengeEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    //가장 멤버가 많이 참여한 챌린지들
    @Query("select c from ChallengeEntity c left join c.memberChallengeEntities d group by c.id order by COUNT(d.id) DESC")
    Page<ChallengeEntity> findChallengesWithMostMembers(Pageable pageable);
}
