package com.example.savingsalt.community.poll.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.poll.domain.PollChoiceEntity;
import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollDto;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.domain.PollResultEntity;
import com.example.savingsalt.community.poll.mapper.PollMainMapper.PollChoiceMapper;
import com.example.savingsalt.community.poll.mapper.PollMainMapper.PollMapper;
import com.example.savingsalt.community.poll.mapper.PollMainMapper.PollResultMapper;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.repository.PollChoiceRepository;
import com.example.savingsalt.community.poll.repository.PollResultRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollChoiceRepository pollChoiceRepository;

    @Autowired
    private PollResultRepository pollResultRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PollMapper pollMapper;

    @Autowired
    private PollChoiceMapper pollChoiceMapper;

    @Autowired
    private PollResultMapper pollResultMapper;

    @Override
    @Transactional
    public PollDto createPoll(Long voteId, PollCreateReqDto pollCreateReqDto) {
        BoardEntity board = boardRepository.findById(pollCreateReqDto.getBoardId())
            .orElseThrow(() -> new RuntimeException("Board not found"));

        PollEntity poll = PollEntity.builder()
            .board(board)
            .build();

        List<PollChoiceEntity> choices = pollCreateReqDto.getChoices().stream()
            .map(choiceDto -> PollChoiceEntity.builder()
                .answer(choiceDto.getAnswer())
                .count(choiceDto.getCount())
                .pollEntity(poll)
                .build())
            .collect(Collectors.toList());

        poll.addChoices(choices);

        PollEntity savedPoll = pollRepository.save(poll);

        return pollMapper.toDto(savedPoll);
    }

    @Override
    @Transactional
    public void deletePoll(Long voteId, Long pollId) {
        pollRepository.deleteById(pollId);
    }

    @Override
    public PollDto getPoll(Long voteId, Long pollId) {
        PollEntity poll = pollRepository.findById(pollId).orElse(null);
        return pollMapper.toDto(poll);
    }

    @Override
    @Transactional
    public PollResultDto participateInPoll(Long voteId, Long pollId, PollChoiceDto choiceDto) {
        PollEntity poll = pollRepository.findById(pollId).orElseThrow(() -> new RuntimeException("Poll not found"));
        PollChoiceEntity choice = pollChoiceRepository.findById(choiceDto.getId()).orElseThrow(() -> new RuntimeException("Choice not found"));

        choice.incrementCount();
        pollChoiceRepository.save(choice);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        MemberEntity member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        PollResultEntity result = new PollResultEntity(null, poll, choice, member);
        PollResultEntity savedResult = pollResultRepository.save(result);

        return pollResultMapper.toDto(savedResult);
    }

    @Override
    public List<PollChoiceDto> getPollResults(Long voteId, Long pollId) {
        PollEntity poll = pollRepository.findById(pollId).orElseThrow(() -> new RuntimeException("Poll not found"));
        List<PollChoiceEntity> choices = pollChoiceRepository.findByPollEntity(poll);
        return choices.stream()
            .map(choice -> pollChoiceMapper.toDto(choice))
            .collect(Collectors.toList());
    }

    @Override
    public PollResDto findPollByBoardId(Long boardId) {
        PollEntity pollEntity = pollRepository.findByBoardId(boardId)
            .orElseThrow(() -> new RuntimeException("Poll not found"));
        List<PollChoiceDto> choices = pollEntity.getChoices().stream()
            .map(pollChoiceMapper::toDto)
            .collect(Collectors.toList());

        return PollResDto.builder()
            .id(pollEntity.getId())
            .boardId(pollEntity.getBoard().getId())
            .choices(choices)
            .build();
    }
}
