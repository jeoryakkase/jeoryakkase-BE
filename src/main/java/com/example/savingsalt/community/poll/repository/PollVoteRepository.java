package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollVoteRepository extends JpaRepository<PollVoteEntity, Long> {
    boolean existsByPollEntityAndMemberEntity_Id(PollEntity pollEntity, Long memberId);
}
