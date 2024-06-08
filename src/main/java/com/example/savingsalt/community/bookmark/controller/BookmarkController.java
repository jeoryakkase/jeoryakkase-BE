package com.example.savingsalt.community.bookmark.controller;

import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.community.bookmark.service.BookmarkService;
import com.example.savingsalt.community.like.exception.LikeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bookmark", description = "북마크 API")
@RestController
@RequestMapping("/api/boards")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @Operation(summary = "포스트 북마크 및 취소", description = "북마크 및 북마크 취소 처리")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "북마크 완료 또는 취소"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음", content = @Content(schema = @Schema(implementation = LikeException.BoardNotFoundException.class))),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음", content = @Content(schema = @Schema(implementation = LikeException.MemberNotFoundException.class)))
    })
    @PostMapping("/{boardId}/bookmark")
    public ResponseEntity<String> bookmarkPost(@PathVariable Long boardId, @RequestBody BookmarkDto bookmarkDto) {
        BookmarkDto dto = BookmarkDto.builder()
            .memberId(bookmarkDto.getMemberId())
            .boardId(boardId)
            .build();
        String message = bookmarkService.bookmarkPost(dto);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "북마크한 게시물 조회", description = "유저가 북마크한 게시물들을 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "북마크한 게시물 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음", content = @Content(schema = @Schema(implementation = LikeException.MemberNotFoundException.class)))
    })
    @GetMapping("/bookmarks/{memberId}")
    public ResponseEntity<List<BookmarkEntity>> getBookmarks(@PathVariable Long memberId) {
        List<BookmarkEntity> bookmarks = bookmarkService.getBookmarks(memberId);
        return ResponseEntity.ok(bookmarks);
    }
}
