package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCertificationRepository extends JpaRepository<GoalCertificationEntity, Long> {

}
