package com.example.savingsalt.member.repository;

import com.example.savingsalt.member.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

}
