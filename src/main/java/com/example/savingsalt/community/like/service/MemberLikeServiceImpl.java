package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.like.domain.LikeReqDto;
import com.example.savingsalt.community.like.domain.LikeResDto;
import com.example.savingsalt.community.like.domain.MemberLikeDto;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.community.like.mapper.MemberLikeMainMapper.LikeMapper;
import com.example.savingsalt.community.like.repository.MemberLikeRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberLikeServiceImpl implements MemberLikeService{

    @Autowired
    private MemberLikeRepository memberLikeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeMapper likeMapper;

    @Override
    @Transactional
    public MemberLikeDto likeBoard(LikeReqDto likeReqDto) {
        BoardEntity board = boardRepository.findById(likeReqDto.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));

        MemberEntity member = memberRepository.findById(likeReqDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        if (memberLikeRepository.existsByBoardEntityAndMemberEntity(board, member)) {
            memberLikeRepository.deleteByBoardEntityAndMemberEntity(board, member);
            return new MemberLikeDto(null, board.getId(), member.getId());
        } else {
            MemberLikeEntity likeEntity = new MemberLikeEntity(board, member);
            MemberLikeEntity savedLikeEntity = memberLikeRepository.save(likeEntity);
            return likeMapper.toDto(savedLikeEntity);
        }
    }

    @Override
    public LikeResDto countLikes(Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        long count = memberLikeRepository.countByBoardEntity(board);
        return new LikeResDto(count);
    }
}
