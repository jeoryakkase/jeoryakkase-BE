package com.example.savingsalt.member.repository;

import com.example.savingsalt.member.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberId(Long memberId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
