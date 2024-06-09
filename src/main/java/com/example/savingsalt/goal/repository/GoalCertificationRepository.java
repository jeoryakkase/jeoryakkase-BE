package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCertificationRepository extends JpaRepository<GoalCertificationEntity, Long> {

    // 목표 엔티티를 기준으로 인증 목록을 인증 날짜 기준 내림차순으로 가져옴
    List<GoalCertificationEntity> findAllByGoalEntityOrderByCertificationDateDesc(
        GoalEntity goalEntity);
}
