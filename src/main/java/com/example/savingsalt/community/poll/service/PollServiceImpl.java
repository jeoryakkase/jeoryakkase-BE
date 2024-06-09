package com.example.savingsalt.community.poll.service;


import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollVoteEntity;
import com.example.savingsalt.community.poll.enums.PollVoteChoice;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.repository.PollVoteRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
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
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public PollEntity createPollForBoard(BoardEntity boardEntity) {
        PollEntity pollEntity = PollEntity.builder()
            .boardEntity(boardEntity)
            .yesCount(0)
            .noCount(0)
            .build();
        return pollRepository.save(pollEntity);
    }

    @Transactional
    public void vote(Long pollId, Long memberId, PollVoteChoice pollVoteChoice) {
        PollEntity pollEntity = pollRepository.findById(pollId).orElseThrow(() -> new RuntimeException("Poll not found"));
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        if (pollVoteRepository.existsByPollEntityAndMemberEntity_Id(pollEntity, memberId)) {
            throw new RuntimeException("User has already voted in this poll");
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
        PollEntity pollEntity = pollRepository.findById(pollId).orElseThrow(() -> new RuntimeException("Poll not found"));
        return PollResultDto.builder()
            .yesCount(pollEntity.getYesCount())
            .noCount(pollEntity.getNoCount())
            .build();
    }

    /// 없앨예정
    public PollResDto findPollByBoardId(Long boardId){return null;}
}
