package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollChoiceEntity;
import com.example.savingsalt.community.poll.domain.PollEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollChoiceRepository extends JpaRepository<PollChoiceEntity, Long> {
    List<PollChoiceEntity> findByPollEntity(PollEntity pollEntity);
}
