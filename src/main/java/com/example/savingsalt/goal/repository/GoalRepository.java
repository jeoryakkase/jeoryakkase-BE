package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {

    // 진행중인 목표의 개수를 반환하는 쿼리
    @Query("SELECT COUNT(g) FROM GoalEntity g WHERE g.memberEntity = :member AND g.goalStatus = :status")
    long countByMemberEntityAndGoalStatus(@Param("member") MemberEntity member,
        @Param("status") GoalStatus status);

    List<GoalEntity> findAllByMemberEntity(MemberEntity member);
}
