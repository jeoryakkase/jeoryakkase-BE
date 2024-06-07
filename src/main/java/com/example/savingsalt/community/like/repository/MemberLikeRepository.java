package com.example.savingsalt.community.like.repository;

import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLikeRepository extends JpaRepository<MemberLikeEntity, Long> {
    Optional<MemberLikeEntity> findByMemberEntityIdAndBoardEntityId(Long memberId, Long boardId);

    int countByBoardEntityId(Long boardId);
}
