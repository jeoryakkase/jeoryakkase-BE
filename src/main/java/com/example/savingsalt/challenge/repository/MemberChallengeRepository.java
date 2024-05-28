package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.MemberChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChallengeRepository extends JpaRepository<MemberChallengeEntity, Long> {

}
