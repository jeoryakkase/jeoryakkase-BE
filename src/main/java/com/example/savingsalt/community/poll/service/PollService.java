package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;

import com.example.savingsalt.community.poll.enums.PollVoteChoice;
import java.time.LocalDateTime;

public interface PollService {
    PollEntity createPollForBoard(Long boardId, LocalDateTime startTime, LocalDateTime endTime);
    void vote(Long pollId, Long memberId, PollVoteChoice pollVoteChoice);
    PollResultDto getPollResults(Long pollId);

    ///이건 없앨예정!!
    PollResDto findPollByBoardId(Long boardId);
}
