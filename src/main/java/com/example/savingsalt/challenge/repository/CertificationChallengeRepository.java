package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.CertificationChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationChallengeRepository extends
    JpaRepository<CertificationChallengeEntity, Long> {

}
