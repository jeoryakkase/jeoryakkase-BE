package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.repository.PollChoiceRepository;
import com.example.savingsalt.community.poll.repository.PollResultRepository;
import com.example.savingsalt.community.poll.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollChoiceRepository pollChoiceRepository;

    @Autowired
    private PollResultRepository pollResultRepository;

    @Override
    public PollResDto createPoll(Long voteId, PollCreateReqDto pollCreateReqDto) {
        return null;
    }

    @Override
    public void deletePoll(Long voteId, Long pollId) {
    }

    @Override
    public PollResDto getPoll(Long voteId, Long pollId) {
        return null;
    }

    @Override
    public PollResultDto participateInPoll(Long voteId, Long pollId, PollChoiceDto choiceDto) {
        return null;
    }

    @Override
    public List<PollResultDto> getPollResults(Long voteId, Long pollId) {
        return Collections.emptyList();
    }
}
