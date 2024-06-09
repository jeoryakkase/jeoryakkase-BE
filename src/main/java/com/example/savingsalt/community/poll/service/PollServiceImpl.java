package com.example.savingsalt.community.poll.service;


import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.VoteEntity;
import com.example.savingsalt.community.poll.enums.VoteChoice;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.repository.VoteRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public PollEntity createPollForBoard(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        PollEntity pollEntity = PollEntity.builder()
            .boardEntity(boardEntity)
            .yesCount(0)
            .noCount(0)
            .build();
        return pollRepository.save(pollEntity);
    }

    @Transactional
    public void vote(Long pollId, Long memberId, VoteChoice voteChoice) {
        PollEntity pollEntity = pollRepository.findById(pollId).orElseThrow(() -> new RuntimeException("Poll not found"));
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        if (voteRepository.existsByPollEntityAndMemberEntity_Id(pollEntity, memberId)) {
            throw new RuntimeException("User has already voted in this poll");
        }

        VoteEntity voteEntity = VoteEntity.builder()
            .pollEntity(pollEntity)
            .memberEntity(memberEntity)
            .voteChoice(voteChoice)
            .build();
        voteRepository.save(voteEntity);

        if (voteChoice == VoteChoice.YES) {
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
