package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;

import com.example.savingsalt.community.poll.enums.VoteChoice;

public interface PollService {
    PollEntity createPollForBoard(Long boardId);
    void vote(Long pollId, Long memberId, VoteChoice voteChoice);
    PollResultDto getPollResults(Long pollId);

    ///이건 없앨예정!!
    PollResDto findPollByBoardId(Long boardId);
}
