package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollResultEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollResultRepository extends JpaRepository<PollResultEntity, Long> {
    List<PollResultEntity> findByPollEntityId(Long pollId);
}
