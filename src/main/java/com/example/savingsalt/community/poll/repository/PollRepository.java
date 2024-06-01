package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.Poll;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {

    Optional<Poll> findByBoardId(Long boardId);

}
