package com.example.savingsalt.badge.repository;

import com.example.savingsalt.badge.domain.entity.MemberGoalBadgeEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGoalBadgeRepository extends JpaRepository<MemberGoalBadgeEntity, Long> {

    List<MemberGoalBadgeEntity> findALlByMemberEntity(MemberEntity memberEntity);
}
