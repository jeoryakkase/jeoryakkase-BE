package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollDto;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollChoiceDto;

import java.util.List;

public interface PollService {
    PollDto createPoll(Long voteId, PollCreateReqDto pollCreateReqDto);
    void deletePoll(Long voteId, Long pollId);
    PollDto getPoll(Long voteId, Long pollId);
    PollResultDto participateInPoll(Long voteId, Long pollId, PollChoiceDto choiceDto);
    List<PollChoiceDto> getPollResults(Long voteId, Long pollId);
    PollResDto findPollByBoardId(Long boardId);
}
