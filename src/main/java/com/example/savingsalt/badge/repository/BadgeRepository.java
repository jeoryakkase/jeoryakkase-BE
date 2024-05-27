package com.example.savingsalt.badge.repository;

import com.example.savingsalt.badge.domain.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

}
