package com.example.savingsalt.community.comment.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.comment.domain.CommentEntity;
import com.example.savingsalt.community.comment.domain.ReplyCommentEntity;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.exception.CommentException;
import com.example.savingsalt.community.comment.exception.CommentException.CommentNotFoundException;
import com.example.savingsalt.community.comment.exception.CommentException.NotFoundParentComment;
import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.community.comment.repository.ReplyCommentRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResDto createComment(CommentReqDto requestDto, MemberEntity member) {

        BoardEntity savedboard = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardException.BoardNotFoundException());

        CommentEntity comment = new CommentEntity(requestDto, savedboard, member);

        return convertToDto(comment);
    }

    @Transactional
    public List<CommentResDto> getCommentsByBoardId(Long boardId) {
        List<CommentEntity> comments = commentRepository.findAllByBoardEntityIdOrderByCreatedAtAsc(
            boardId);
        return comments.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<String> updateComment(Long commentId, CommentReqDto requestDto,
        MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        // 작성자만 수정 가능
        if (!comment.getMemberEntity().getId().equals(member.getId())) {
            throw new CommentException.ValidateAuthorForUpdate();
        }
        // 대댓글이 없는 댓글만 수정 가능
        if (!comment.getReplyComments().isEmpty()) {
            throw new CommentException.ValidateAuthorForUpdate();
        }

        comment.update(requestDto);
        commentRepository.save(comment);
        return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteComment(Long commentId, MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        // 작성자만 삭제 가능
        if (!comment.getMemberEntity().getId().equals(member.getId())) {
            throw new CommentException.ValidateAuthorForDelete();
        }

        commentRepository.delete(comment);
        return new ResponseEntity<>("댓글 삭제에 성공했습니다.", HttpStatus.OK);
    }

    @Transactional
    public ReplyCommentResDto createReplyComment(ReplyCommentReqDto requestDto,
        MemberEntity member) {
        CommentEntity parentComment = commentRepository.findById(requestDto.getParentCommentId())
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 부모 comment id"));

        ReplyCommentEntity replyComment = new ReplyCommentEntity(requestDto.getContent(),
            parentComment, member);

        ReplyCommentEntity savedReplyComment = replyCommentRepository.save(replyComment);

        return convertToReplyDto(savedReplyComment);
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
        replyCommentRepository.save(replyComment);
        return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
    }


    // 선택한 댓글 존재 여부
    private CommentEntity findComment(Long CommentId) {
        return commentRepository.findById(CommentId)
            .orElseThrow(() -> new CommentNotFoundException());
    }

    // 선택한 대댓글 존재 여부
    private ReplyCommentEntity findReply(Long replyId) {
        return replyCommentRepository.findById(replyId)
            .orElseThrow(() -> new CommentNotFoundException());
    }

    private CommentResDto convertToDto(CommentEntity comment) {
        List<ReplyCommentResDto> replyComments = comment.getReplyComments().stream()
            .map(this::convertToReplyDto)
            .collect(Collectors.toList());

        return CommentResDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .nickname(comment.getNickname())
            .replyComments(replyComments)
            .build();
    }

    private ReplyCommentResDto convertToReplyDto(ReplyCommentEntity replyComment) {
        return ReplyCommentResDto.builder()
            .id(replyComment.getId())
            .parentCommentId(replyComment.getParentComment().getId())
            .memberId(replyComment.getMemberEntity().getId())
            .build();
    }


}
