package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationChallengeRepository extends
    JpaRepository<CertificationChallengeEntity, Long> {

    List<CertificationChallengeEntity> findAllByMemberChallengeEntity(
        MemberChallengeEntity memberChallengeEntity);

}
