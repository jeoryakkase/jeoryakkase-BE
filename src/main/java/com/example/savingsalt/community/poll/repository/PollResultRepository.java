package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollResultRepository extends JpaRepository<PollResult, Long> {

}
