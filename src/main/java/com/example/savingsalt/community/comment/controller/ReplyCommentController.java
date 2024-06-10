package com.example.savingsalt.community.comment.controller;

import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.service.ReplyCommentService;
import com.example.savingsalt.global.UnauthorizedException;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "대댓글 작성 성공", content = @Content(schema = @Schema(implementation = ReplyCommentResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "부모 댓글을 찾을 수 없음")
    })
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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "대댓글 수정 성공",
            content = @Content(schema = @Schema(implementation = ReplyCommentResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "대댓글 수정 권한 없음")
    })
    @PatchMapping("/{replyId}")
    public ResponseEntity<ReplyCommentResDto> updateReplyComment(
        @PathVariable Long replyId,
        @RequestBody ReplyCommentReqDto requestDto,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        ReplyCommentResDto replyCommentResDto = replyCommentService.updateReplyComment(replyId,
            requestDto, member);
        return ResponseEntity.ok(replyCommentResDto);
    }

    @Operation(summary = "대댓글 삭제", description = "로그인된 사용자가 특정 대댓글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "대댓글 삭제 성공",
            content = @Content(schema = @Schema(implementation = ReplyCommentResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "대댓글 삭제 권한 없음")
    })
    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReplyComment(
        @PathVariable Long replyId,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        replyCommentService.deleteReplyComment(replyId, member);

        return ResponseEntity.ok("대댓글이 삭제되었습니다.");
    }


}
