package com.example.savingsalt.community.comment.controller;

import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.service.CommentService;
import com.example.savingsalt.global.UnauthorizedException;
import com.example.savingsalt.member.domain.MemberEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "로그인된 사용자가 새로운 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<CommentResDto> createComment(@RequestBody CommentReqDto requestDto,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        CommentResDto responseDto = commentService.createComment(requestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "댓글 수정", description = "댓글 ID를 통해 댓글의 내용을 수정합니다.")
    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
        @RequestBody CommentReqDto requestDto, @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return commentService.updateComment(commentId, requestDto, member);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID를 통해 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return commentService.deleteComment(commentId, member);
    }
}
