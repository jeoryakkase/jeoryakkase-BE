package com.example.savingsalt.community.comment.controller;

import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.service.ReplyCommentService;
import com.example.savingsalt.global.UnauthorizedException;
import com.example.savingsalt.member.domain.entity.MemberEntity;
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

@Tag(name = "ReplyComment", description = "대댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/replies")
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    @Operation(summary = "대댓글 작성", description = "로그인된 사용자가 댓글에 대댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ReplyCommentResDto> createReplyComment(
        @RequestBody ReplyCommentReqDto requestDto,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        ReplyCommentResDto responseDto = replyCommentService.createReplyComment(requestDto, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "대댓글 수정", description = "로그인된 사용자가 특정 대댓글을 수정합니다.")
    @PatchMapping("/{replyId}")
    public ResponseEntity<String> updateReplyComment(
        @PathVariable Long replyCommentId,
        @RequestBody ReplyCommentReqDto requestDto,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return replyCommentService.updateReplyComment(replyCommentId, requestDto, member);
    }

    @Operation(summary = "대댓글 삭제", description = "로그인된 사용자가 특정 대댓글을 삭제합니다.")
    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReplyComment(
        @PathVariable Long replyCommentId,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return replyCommentService.deleteReplyComment(replyCommentId, member);
    }


}
