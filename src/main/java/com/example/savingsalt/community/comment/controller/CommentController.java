package com.example.savingsalt.community.comment.controller;

import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.service.CommentService;
import com.example.savingsalt.global.UnauthorizedException;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.service.MemberService;
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
import org.springframework.security.core.userdetails.UserDetails;
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

    private final MemberService memberService;

    private final MemberMapper memberMapper;

    @Operation(summary = "댓글 작성", description = "로그인된 사용자가 새로운 댓글을 작성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "댓글 작성 성공", content = @Content(schema = @Schema(implementation = CommentResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<CommentResDto> createComment(@RequestBody CommentReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(
            memberService.findMemberByEmail(userDetails.getUsername()));

        CommentResDto responseDto = commentService.createComment(requestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @Operation(summary = "댓글 수정", description = "댓글 ID를 통해 댓글의 내용을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
            content = @Content(schema = @Schema(implementation = CommentResDto.class))),
        @ApiResponse(responseCode = "400", description = "댓글 수정 불가(대댓글 존재)"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "댓글 수정 권한 없음")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResDto> updateComment(@PathVariable("id") Long id,
        @RequestBody CommentReqDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(
            memberService.findMemberByEmail(userDetails.getUsername()));

        CommentResDto commentResDto = commentService.updateComment(id, requestDto, member);

        return ResponseEntity.ok(commentResDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID를 통해 댓글과 대댓글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "댓글 삭제 권한 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(
            memberService.findMemberByEmail(userDetails.getUsername()));
        commentService.deleteComment(id, member);

        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PostMapping("/replies")
    public ResponseEntity<ReplyCommentResDto> createReplyComment(
        @RequestBody ReplyCommentReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(
            memberService.findMemberByEmail(userDetails.getUsername()));

        ReplyCommentResDto responseDto = commentService.createReplyComment(requestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

}