package com.example.savingsalt.community.comment.service;

import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.exception.CommentException;
import com.example.savingsalt.community.comment.domain.CommentEntity;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentService replyCommentRepository;

    public CommentResDto createComment(CommentReqDto requestDto, MemberEntity member) {

        BoardEntity savedBoard = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardException.BoardNotFoundException());

        return new CommentResDto(
            commentRepository.save(new CommentEntity(requestDto, savedBoard, member)));
    }



    public ResponseEntity<String> updateComment(Long commentId, CommentReqDto requestDto,
        MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        if (comment.getNickname().equals(member.getNickname())) {
            comment.update(requestDto);
            commentRepository.save(comment);
            return new ResponseEntity<>("댓글 수정에 성공했습니다.", HttpStatus.OK);
        } else {
            throw new CommentException.ValidateAuthorForUpdate();
        }
    }

    public ResponseEntity<String> deleteComment(Long commentId, MemberEntity member) {
        CommentEntity comment = findComment(commentId);

        if (comment.getNickname().equals(member.getNickname())) {
            commentRepository.delete(comment);

            return new ResponseEntity<>("댓글 삭제에 성공했습니다.", HttpStatus.OK);
        } else {
            throw new CommentException.ValidateAuthorForUpdate();
        }
    }

    // 선택한 댓글 존재 여부
    private CommentEntity findComment(Long CommentId) {
        return commentRepository.findById(CommentId)
            .orElseThrow(() -> new CommentException.CommentNotFoundException());
    }


}
