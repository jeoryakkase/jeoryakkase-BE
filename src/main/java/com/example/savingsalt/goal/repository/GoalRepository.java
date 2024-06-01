package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {

}
