package com.example.savingsalt.badge.repository;

import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

    List<BadgeEntity> findAll();
}
