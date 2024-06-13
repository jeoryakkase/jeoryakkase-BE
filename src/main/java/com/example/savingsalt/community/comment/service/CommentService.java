package com.example.savingsalt.community.comment.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.domain.entity.CommentEntity;
import com.example.savingsalt.community.comment.domain.entity.ReplyCommentEntity;
import com.example.savingsalt.community.comment.exception.CommentException;
import com.example.savingsalt.community.comment.exception.CommentException.CommentNotFoundException;
import com.example.savingsalt.community.comment.exception.CommentException.ValidateAuthorForDelete;
import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.community.comment.repository.ReplyCommentRepository;
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
    private final ReplyCommentRepository replyCommentRepository;

    @Transactional
    public CommentResDto createComment(CommentReqDto requestDto, MemberEntity member) {

        BoardEntity savedboard = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardException.BoardNotFoundException());

        CommentEntity comment = new CommentEntity(requestDto, savedboard, member);

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
        if (replyCommentRepository.existsByParentComment(comment)) {
            throw new CommentException.CannotUpdateCommentWithReplies();
        }

        CommentEntity updateComment = comment.toBuilder()
            .content(requestDto.getContent())
            .build();

        commentRepository.save(updateComment);

        return convertToDto(updateComment);
    }

    @Transactional
    public void deleteComment(Long commentId, MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        // 작성자만 삭제 가능
        if (!comment.getMemberEntity().getId().equals(member.getId())) {
            throw new ValidateAuthorForDelete();
        }

        // 대댓글 삭제
        List<ReplyCommentEntity> replyComments = replyCommentRepository.findByParentComment(comment);
        replyCommentRepository.deleteAll(replyComments);

        // 댓글 삭제
        commentRepository.delete(comment);

    }

    // 선택한 댓글 존재 여부
    private CommentEntity findComment(Long CommentId) {
        return commentRepository.findById(CommentId)
            .orElseThrow(() -> new CommentNotFoundException());
    }

    private CommentResDto convertToDto(CommentEntity comment) {

        return CommentResDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .nickname(comment.getMemberEntity().getNickname())
            .build();
    }
}
