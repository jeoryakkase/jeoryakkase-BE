package com.example.savingsalt.community.like.repository;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLikeRepository extends JpaRepository<MemberLikeEntity, Long> {

    int countByBoardEntityId(Long boardId);

    Optional<MemberLikeEntity> findByMemberEntityAndBoardEntity(MemberEntity memberEntity, BoardEntity boardEntity);
}
