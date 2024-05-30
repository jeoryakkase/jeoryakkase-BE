package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollChoiceRepository extends JpaRepository<PollChoiceEntity, Long> {

}
