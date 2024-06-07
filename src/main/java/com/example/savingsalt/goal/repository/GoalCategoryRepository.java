package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalCategoryEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCategoryRepository extends JpaRepository<GoalCategoryEntity, Long> {
    List<GoalCategoryEntity> findByMemberEntity(MemberEntity memberEntity);
}
