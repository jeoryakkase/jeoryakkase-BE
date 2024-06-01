package com.example.savingsalt.community.board.repository;

import com.example.savingsalt.community.board.domain.CommentEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.boardEntity.id = :boardId ORDER BY c.createdAt DESC ")
    List<CommentEntity> findAllByBoardEntityOrderByCreatedAtDesc(@Param("boardId") Long boardId);
}
