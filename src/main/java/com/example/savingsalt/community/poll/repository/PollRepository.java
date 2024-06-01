package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<PollEntity, Long> {
    Optional<PollEntity> findByBoardId(Long boardId);
}
