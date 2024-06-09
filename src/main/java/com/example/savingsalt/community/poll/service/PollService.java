package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;

import com.example.savingsalt.community.poll.enums.PollVoteChoice;

public interface PollService {
    PollEntity createPollForBoard(BoardEntity boardEntity);
    void vote(Long pollId, Long memberId, PollVoteChoice voteChoice);
    PollResultDto getPollResults(Long pollId);

    ///이건 없앨예정!!
    PollResDto findPollByBoardId(Long boardId);
}
