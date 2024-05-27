package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollChoiceDto;

import java.util.List;

public interface PollService {
    PollResDto createPoll(Long voteId, PollCreateReqDto pollCreateReqDto);
    void deletePoll(Long voteId, Long pollId);
    PollResDto getPoll(Long voteId, Long pollId);
    PollResultDto participateInPoll(Long voteId, Long pollId, PollChoiceDto choiceDto);
    List<PollResultDto> getPollResults(Long voteId, Long pollId);
}
