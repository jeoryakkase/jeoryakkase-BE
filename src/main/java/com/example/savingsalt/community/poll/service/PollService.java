package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResultDto;

import com.example.savingsalt.community.poll.enums.PollVoteChoice;

public interface PollService {
    PollEntity createPollForBoard(Long boardId);
    void vote(Long pollId, String email, PollVoteChoice pollVoteChoice);
    PollResultDto getPollResults(Long pollId);

}
