package com.example.savingsalt.community.comment.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.domain.entity.CommentEntity;
import com.example.savingsalt.community.comment.exception.CommentException;
import com.example.savingsalt.community.comment.exception.CommentException.CommentNotFoundException;
import com.example.savingsalt.community.comment.exception.CommentException.ValidateAuthorForDelete;
import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResDto createComment(CommentReqDto requestDto, MemberEntity member) {

        BoardEntity savedboard = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardException.BoardNotFoundException());

        CommentEntity comment = new CommentEntity(requestDto, savedboard, member, null, 0, 0L);

        commentRepository.save(comment);

        return convertToDto(comment);
    }

    @Transactional
    public CommentResDto updateComment(Long commentId, CommentReqDto requestDto,
        MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        // 작성자만 수정 가능
        if (!comment.getMemberEntity().getId().equals(member.getId())) {
            throw new CommentException.ValidateAuthorForUpdate();
        }
        // 대댓글이 없는 댓글만 수정 가능
        if (commentRepository.existsByParentComment(comment)) {
            throw new CommentException.CannotUpdateCommentWithReplies();
        }

        comment = comment.toBuilder()
            .content(requestDto.getContent())
            .build();

        CommentEntity updatedComment = commentRepository.save(comment);

        return convertToDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        // 작성자만 삭제 가능
        if (!comment.getMemberEntity().getId().equals(member.getId())) {
            throw new ValidateAuthorForDelete();
        }

        if (commentRepository.existsByParentComment(comment)) {
            // 대댓글이 있는 경우 재귀적으로 삭제
            deleteCommentAndReplies(comment);
        }
        // 대댓글이 없는 경우 바로 삭제
        commentRepository.delete(comment);

    }

    @Transactional
    public ReplyCommentResDto createReplyComment(ReplyCommentReqDto requestDto,
        MemberEntity member) {

        BoardEntity savedboard = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardException.BoardNotFoundException());

        CommentEntity parentComment = commentRepository.findById(requestDto.getParentCommentId())
            .orElseThrow(() -> new EntityNotFoundException("부모 댓글을 찾을 수 없습니다."));

        int depth = parentComment.getDepth() + 1;
        Long level = parentComment.getId();

        CommentEntity replyComment = CommentEntity.builder()
            .content(requestDto.getContent())
            .memberEntity(member)
            .boardEntity(savedboard)
            .parentComment(parentComment)
            .depth(depth)
            .level(level)
            .build();

        CommentEntity savedReplyComment = commentRepository.save(replyComment);

        return convertToReplyDto(savedReplyComment);
    }

    // 선택한 댓글 존재 여부
    private CommentEntity findComment(Long CommentId) {
        return commentRepository.findById(CommentId)
            .orElseThrow(() -> new CommentNotFoundException());
    }

    private CommentResDto convertToDto(CommentEntity comment) {
        List<CommentResDto> replyComments = commentRepository.findAllByParentComment(comment)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

        return CommentResDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .replyComments(replyComments)
            .nickname(comment.getMemberEntity().getNickname())
            .level(comment.getLevel())
            .depth(comment.getDepth())
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build();
    }

    private ReplyCommentResDto convertToReplyDto(CommentEntity replyComment) {
        return ReplyCommentResDto.builder()
            .id(replyComment.getId())
            .parentCommentId(replyComment.getParentComment().getId())
            .nickname(replyComment.getMemberEntity().getNickname())
            .content(replyComment.getContent())
            .createdAt(replyComment.getCreatedAt())
            .modifiedAt(replyComment.getModifiedAt())
            .build();
    }

    private void deleteCommentAndReplies(CommentEntity comment) {
        List<CommentEntity> replyComments = commentRepository.findAllByParentComment(comment);
        for (CommentEntity replyComment : replyComments) {
            deleteCommentAndReplies(replyComment);
            commentRepository.delete(replyComment);
        }
    }
}
