package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.like.exception.LikeException.BoardNotFoundException;
import com.example.savingsalt.community.like.exception.LikeException.MemberNotFoundException;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.community.like.repository.MemberLikeRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.Optional;
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

    @Override
    @Transactional
    public String likePost(String email, Long boardId) {
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        BoardEntity boardEntity = boardRepository.findById(boardId)
            .orElseThrow(BoardNotFoundException::new);

        Optional<MemberLikeEntity> likeOpt = memberLikeRepository.findByMemberEntityAndBoardEntity(memberEntity, boardEntity);

        if (likeOpt.isPresent()) {
            memberLikeRepository.delete(likeOpt.get());
            boardEntity.decrementLikes();
            boardRepository.save(boardEntity);
            return "좋아요 취소";
        } else {
            MemberLikeEntity like = new MemberLikeEntity(boardEntity, memberEntity);
            memberLikeRepository.save(like);
            boardEntity.incrementLikes();
            boardRepository.save(boardEntity);
            return "좋아요 완료";
        }
    }

    @Override
    public int countLikes(Long boardId) {
        return memberLikeRepository.countByBoardEntityId(boardId);
    }
}
