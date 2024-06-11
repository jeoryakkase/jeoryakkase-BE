package com.example.savingsalt.community.poll.service;


import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollVoteEntity;
import com.example.savingsalt.community.poll.enums.PollVoteChoice;
import com.example.savingsalt.community.poll.exception.PollException;
import com.example.savingsalt.community.poll.exception.PollException.MemberNotFoundException;
import com.example.savingsalt.community.poll.exception.PollException.PollNotFoundException;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.repository.PollVoteRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public PollEntity createPollForBoard(Long boardId, LocalDateTime startTime, LocalDateTime endTime) {
        BoardEntity boardEntity = boardRepository.findById(boardId)
            .orElseThrow(PollNotFoundException::new);

        PollEntity pollEntity = PollEntity.builder()
            .boardEntity(boardEntity)
            .yesCount(0)
            .noCount(0)
            .startTime(startTime)
            .endTime(endTime)
            .build();
        return pollRepository.save(pollEntity);
    }

    @Transactional
    public void vote(Long pollId, String email, PollVoteChoice pollVoteChoice) {
        PollEntity pollEntity = pollRepository.findById(pollId).orElseThrow(
            PollNotFoundException::new);
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElseThrow(
            MemberNotFoundException::new);

        if (pollVoteRepository.existsByPollEntityAndMemberEntity_Id(pollEntity, memberEntity.getId())) {
            throw new PollException.UserAlreadyVotedException();
        }

        if (!pollEntity.isActive()) {
            throw new PollException.PollNotActiveException();
        }

        PollVoteEntity pollVoteEntity = PollVoteEntity.builder()
            .pollEntity(pollEntity)
            .memberEntity(memberEntity)
            .pollVoteChoice(pollVoteChoice)
            .build();
        pollVoteRepository.save(pollVoteEntity);

        if (pollVoteChoice == PollVoteChoice.YES) {
            pollEntity = PollEntity.builder()
                .yesCount(pollEntity.getYesCount() + 1)
                .noCount(pollEntity.getNoCount())
                .boardEntity(pollEntity.getBoardEntity())
                .build();
        } else {
            pollEntity = PollEntity.builder()
                .yesCount(pollEntity.getYesCount())
                .noCount(pollEntity.getNoCount() + 1)
                .boardEntity(pollEntity.getBoardEntity())
                .build();
        }

        pollRepository.save(pollEntity);
    }

    @Transactional(readOnly = true)
    public PollResultDto getPollResults(Long pollId) {
        PollEntity pollEntity = pollRepository.findById(pollId).orElseThrow(
            PollNotFoundException::new);

        if (pollEntity.isNotStarted()) {
            return PollResultDto.builder()
                .yesCount(0)
                .noCount(0)
                .isFinished(false)
                .build();
        }

        return PollResultDto.builder()
            .yesCount(pollEntity.getYesCount())
            .noCount(pollEntity.getNoCount())
            .isFinished(pollEntity.isFinished())
            .build();
    }

    /// 없앨예정
    public PollResDto findPollByBoardId(Long boardId){return null;}
}
