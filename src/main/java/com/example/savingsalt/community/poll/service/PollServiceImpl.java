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
import com.example.savingsalt.community.poll.exception.PollException;
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
    public PollDto createPoll(PollCreateReqDto pollCreateReqDto) {
        BoardEntity board = pollCreateReqDto.getBoard();

        if (board == null) {
            throw new PollException.PollCreationException("투표 생성에 실패하였습니다.");
        }

        PollEntity poll = pollCreateReqDto.toEntity(board);

        PollEntity savedPoll = pollRepository.save(poll);

        return pollMapper.toDto(savedPoll);
    }

    @Override
    @Transactional
    public void deletePoll(Long voteId, Long pollId) {
        PollEntity poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new PollException.PollNotFoundException("해당 투표를 찾을 수 없습니다."));
        pollRepository.delete(poll);
    }

    @Override
    public PollDto getPoll(Long voteId, Long pollId) {
        PollEntity poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new PollException.PollNotFoundException("해당 투표를 찾을 수 없습니다."));
        return pollMapper.toDto(poll);
    }

    @Override
    @Transactional
    public PollResultDto participateInPoll(Long voteId, Long pollId, PollChoiceDto choiceDto) {
        PollEntity poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new PollException.PollNotFoundException("해당 투표를 찾을 수 없습니다."));
        PollChoiceEntity choice = pollChoiceRepository.findById(choiceDto.getId())
            .orElseThrow(() -> new PollException.ChoiceNotFoundException("선택지를 찾을 수 없습니다."));

        choice.incrementCount();
        pollChoiceRepository.save(choice);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        MemberEntity member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new PollException.UnauthorizedPollAccessException("해당 투표에 접근할 권한이 없습니다."));
        PollResultEntity result = new PollResultEntity(null, poll, choice, member);
        PollResultEntity savedResult = pollResultRepository.save(result);

        return pollResultMapper.toDto(savedResult);
    }

    @Override
    public List<PollChoiceDto> getPollResults(Long voteId, Long pollId) {
        PollEntity poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new PollException.PollNotFoundException("해당 투표를 찾을 수 없습니다."));
        List<PollChoiceEntity> choices = pollChoiceRepository.findByPollEntity(poll);
        return choices.stream()
            .map(choice -> pollChoiceMapper.toDto(choice))
            .collect(Collectors.toList());
    }

    @Override
    public PollResDto findPollByBoardId(Long boardId) {
        PollEntity pollEntity = pollRepository.findByBoardId(boardId)
            .orElseThrow(() -> new PollException.PollNotFoundException("해당 투표를 찾을 수 없습니다."));
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
