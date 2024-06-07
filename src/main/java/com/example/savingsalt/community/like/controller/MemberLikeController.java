package com.example.savingsalt.community.like.controller;

import com.example.savingsalt.community.like.LikeException;
import com.example.savingsalt.community.like.domain.MemberLikeDto;
import com.example.savingsalt.community.like.service.MemberLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요", description = "좋아요 API")
@RestController
@RequestMapping("/api/boards")
public class MemberLikeController {

    @Autowired
    private MemberLikeService memberLikeService;

    @Operation(summary = "포스트 좋아요 및 취소", description = "좋아요 및 좋아요 취소 처리")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "좋아요 완료 또는 취소"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음", content = @Content(schema = @Schema(implementation = LikeException.BoardNotFoundException.class))),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음", content = @Content(schema = @Schema(implementation = LikeException.MemberNotFoundException.class)))
    })
    @PostMapping("/{boardId}/likes")
    public ResponseEntity<String> likePost(@PathVariable Long boardId, @RequestBody MemberLikeDto memberLikeDto) {
        MemberLikeDto dto = MemberLikeDto.builder()
            .memberId(memberLikeDto.getMemberId())
            .boardId(boardId)
            .build();
        String message = memberLikeService.likePost(dto);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "포스트 좋아요 수", description = "포스트에 누적된 좋아요 수 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "좋아요 수 조회 성공"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음", content = @Content(schema = @Schema(implementation = LikeException.BoardNotFoundException.class)))
    })
    @GetMapping("/{boardId}/likes-count")
    public ResponseEntity<Integer> countLikes(@PathVariable Long boardId) {
        int count = memberLikeService.countLikes(boardId);
        return ResponseEntity.ok(count);
    }
}
