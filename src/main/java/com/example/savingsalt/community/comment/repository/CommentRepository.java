package com.example.savingsalt.community.comment.repository;

import com.example.savingsalt.community.comment.domain.entity.CommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByBoardEntityIdOrderByCreatedAtAsc(Long boardId);

}

