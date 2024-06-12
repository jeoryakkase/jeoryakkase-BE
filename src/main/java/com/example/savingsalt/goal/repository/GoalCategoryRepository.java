package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalCategoryEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCategoryRepository extends JpaRepository<GoalCategoryEntity, Long> {

    Page<GoalCategoryEntity> findByMemberEntity(MemberEntity memberEntity, Pageable pageable);
}
