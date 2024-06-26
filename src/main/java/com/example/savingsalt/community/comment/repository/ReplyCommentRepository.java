package com.example.savingsalt.community.comment.repository;

import com.example.savingsalt.community.comment.domain.entity.CommentEntity;
import com.example.savingsalt.community.comment.domain.entity.ReplyCommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyCommentEntity, Long> {

    List<ReplyCommentEntity> findAllByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    boolean existsByParentComment(CommentEntity parentComment);

    List<ReplyCommentEntity> findByParentComment(CommentEntity parentComment);
}
