package com.example.savingsalt.challenge.repository;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChallengeRepository extends JpaRepository<MemberChallengeEntity, Long> {

    List<MemberChallengeEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
