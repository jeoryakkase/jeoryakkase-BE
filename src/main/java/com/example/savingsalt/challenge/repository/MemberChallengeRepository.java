package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChallengeRepository extends JpaRepository<MemberChallengeEntity, Long> {

    List<MemberChallengeEntity> findAllByMemberEntity(MemberEntity memberEntity);

    @Query("SELECT mc FROM MemberChallengeEntity mc "
        + "JOIN FETCH mc.certificationChallengeEntities "
        + "JOIN FETCH mc.challengeEntity ")
    List<MemberChallengeEntity> findAllWithFetchJoinByMemberEntity(MemberEntity memberEntity);

    List<MemberChallengeEntity> findAllByChallengeEntity(ChallengeEntity challengeEntity);

    List<MemberChallengeEntity> findAllByMemberEntityAndChallengeStatus(MemberEntity memberEntity,
        ChallengeStatus challengeStatus);
}
