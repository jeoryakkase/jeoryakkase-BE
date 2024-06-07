package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.like.domain.MemberLikeDto;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.community.like.repository.MemberLikeRepository;
import com.example.savingsalt.member.domain.MemberEntity;
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
    public String likePost(MemberLikeDto memberLikeDto) {
        Long memberId = memberLikeDto.getMemberId();
        Long boardId = memberLikeDto.getBoardId();

        Optional<MemberLikeEntity> likeOpt = memberLikeRepository.findByMemberEntityIdAndBoardEntityId(memberId, boardId);

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (likeOpt.isPresent()) {
            memberLikeRepository.delete(likeOpt.get());
            boardEntity.decrementLikes(); // 좋아요 수 감소
            boardRepository.save(boardEntity);
            return "좋아요 취소";
        } else {
            MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
            MemberLikeEntity like = new MemberLikeEntity(boardEntity, memberEntity);
            memberLikeRepository.save(like);
            boardEntity.incrementLikes(); // 좋아요 수 증가
            boardRepository.save(boardEntity);
            return "좋아요 완료";
        }
    }

    @Override
    public int countLikes(Long boardId) {
        return memberLikeRepository.countByBoardEntityId(boardId);
    }
}
