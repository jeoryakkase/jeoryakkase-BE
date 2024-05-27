package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollChoiceRepository extends JpaRepository<PollChoice, Long> {

}
