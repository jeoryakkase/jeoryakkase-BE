package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.CertificationChallengeImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationChallengeImageRepository extends JpaRepository<CertificationChallengeImageEntity, Long> {
}
