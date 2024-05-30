package com.example.savingsalt.community.like.repository;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLikeRepository extends JpaRepository<MemberLikeEntity, Long> {
    boolean existsByBoardEntityAndMemberEntity(BoardEntity boardEntity, MemberEntity memberEntity);
    void deleteByBoardEntityAndMemberEntity(BoardEntity boardEntity, MemberEntity memberEntity);
    long countByBoardEntity(BoardEntity boardEntity);
}
