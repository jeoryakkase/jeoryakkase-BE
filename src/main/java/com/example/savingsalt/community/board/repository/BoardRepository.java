package com.example.savingsalt.community.board.repository;

import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findAllByCategoryOrderByCreatedAtDesc(BoardCategory category);

    @Query("SELECT b FROM BoardEntity b JOIN FETCH MemberChallengeEntity m ON b.memberEntity.id = m.memberEntity.id WHERE b.category = :category AND m.challengeStatus = 'COMPLETED' ORDER BY b.createdAt DESC")
    List<BoardEntity> findAllHofBoards(BoardCategory category);

    Optional<BoardEntity> findByIdAndCategory(Long id, BoardCategory category);

}
