package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.like.domain.LikeReqDto;
import com.example.savingsalt.community.like.domain.LikeResDto;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.community.like.repository.MemberLikeRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberLikeServiceImpl implements MemberLikeService{

    @Autowired
    private MemberLikeRepository memberLikeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public void likeBoard(LikeReqDto likeReqDto){
        BoardEntity board = boardRepository.findById(likeReqDto.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));

        MemberEntity member = memberRepository.findById(likeReqDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        if (memberLikeRepository.existsByBoardEntityAndMemberEntity(board, member)) {
            memberLikeRepository.deleteByBoardEntityAndMemberEntity(board, member);
        } else {
            MemberLikeEntity like = new MemberLikeEntity(board, member);
            memberLikeRepository.save(like);
        }
    }

    @Override
    public LikeResDto countLikes(Long boardId){
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        long count = memberLikeRepository.countByBoardEntity(board);
        return new LikeResDto(count);
    }

}
