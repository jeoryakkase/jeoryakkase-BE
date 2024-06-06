package com.example.savingsalt.community.comment.controller;

import com.example.savingsalt.community.board.exception.BoardException.UnauthorizedCreateException;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.service.CommentService;
import com.example.savingsalt.member.domain.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResDto> createComment(@RequestBody CommentReqDto requestDto,
        @AuthenticationPrincipal
        MemberEntity member) {
        if (member == null) {
            throw new UnauthorizedCreateException();
        }

        CommentReqDto updateReqDto = CommentReqDto.builder()
            .content(requestDto.getContent())
            .boardId(requestDto.getBoardId())
            .memberId(member.getId())
            .build();

        CommentResDto responseDto = commentService.createComment(updateReqDto, member);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
        @RequestBody CommentReqDto requestDto, @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedCreateException();
        }

        return commentService.updateComment(commentId, requestDto, member);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
        @AuthenticationPrincipal MemberEntity member) {
        return commentService.deleteComment(commentId, member);
    }

}
