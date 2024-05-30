package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {

}
