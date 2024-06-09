package com.example.savingsalt.community.poll.repository;

import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    boolean existsByPollEntityAndMemberEntity_Id(PollEntity pollEntity, Long memberId);
}
