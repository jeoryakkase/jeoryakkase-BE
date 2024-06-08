package com.example.savingsalt.community.comment.service;

import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.domain.entity.CommentEntity;
import com.example.savingsalt.community.comment.domain.entity.ReplyCommentEntity;
import com.example.savingsalt.community.comment.exception.CommentException;
import com.example.savingsalt.community.comment.exception.CommentException.CommentNotFoundException;
import com.example.savingsalt.community.comment.exception.CommentException.NotFoundParentComment;
import com.example.savingsalt.community.comment.exception.CommentException.ValidateAuthorForDelete;
import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.community.comment.repository.ReplyCommentRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyCommentService {

    private final CommentRepository commentRepository;

    private final ReplyCommentRepository replyCommentRepository;

    @Transactional
    public ReplyCommentResDto createReplyComment(ReplyCommentReqDto requestDto,
        MemberEntity member) {
        CommentEntity parentComment = commentRepository.findById(requestDto.getParentCommentId())
            .orElseThrow(() -> new NotFoundParentComment());

        ReplyCommentEntity replyComment = new ReplyCommentEntity(requestDto.getContent(),
            parentComment, member);

        replyCommentRepository.save(replyComment);

        return convertToReplyDto(replyComment);
    }

    @Transactional
    public ResponseEntity<String> updateReplyComment(Long replyId, ReplyCommentReqDto requestDto,
        MemberEntity member) {

        CommentEntity parentComment = commentRepository.findById(requestDto.getParentCommentId())
            .orElseThrow(() -> new NotFoundParentComment());

        ReplyCommentEntity replyComment = findReply(replyId);

        // 작성자만 수정 가능
        if (!replyComment.getMemberEntity().getId().equals(member.getId())) {
            throw new CommentException.ValidateAuthorForUpdate();
        }

        replyComment.update(requestDto);
        return new ResponseEntity<>("대댓글이 수정되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteReplyComment(Long replyId, MemberEntity member) {
        ReplyCommentEntity reply = findReply(replyId);

        if (!reply.getMemberEntity().getId().equals(member.getId())) {
            throw new ValidateAuthorForDelete();
        }
        replyCommentRepository.delete(reply);

        return new ResponseEntity<>("대댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    // 선택한 대댓글 존재 여부
    private ReplyCommentEntity findReply(Long replyId) {
        return replyCommentRepository.findById(replyId)
            .orElseThrow(() -> new CommentNotFoundException());
    }

    private ReplyCommentResDto convertToReplyDto(ReplyCommentEntity replyComment) {
        return ReplyCommentResDto.builder()
            .id(replyComment.getId())
            .parentCommentId(replyComment.getParentComment().getId())
            .memberId(replyComment.getMemberEntity().getId())
            .build();
    }


}
