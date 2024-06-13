package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChallengeRepository extends JpaRepository<MemberChallengeEntity, Long> {

    List<MemberChallengeEntity> findAllByMemberEntity(MemberEntity memberEntity);

    MemberChallengeEntity findByMemberEntity(MemberEntity memberEntity);

    List<MemberChallengeEntity> findAllByChallengeEntity(ChallengeEntity challengeEntity);

    @Query("SELECT COUNT(c) FROM MemberChallengeEntity c WHERE c.challengeStatus = :challengeStatus AND c.challengeEntity = : challengeEntity")
    long countByChallengeStatusAndChallengeEntity(ChallengeStatus challengeStatus, ChallengeEntity challengeEntity);
}
