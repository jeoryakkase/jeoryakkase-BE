package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<PollEntity, Long> {

}
