package com.example.savingsalt.community.bookmark.controller;

import com.example.savingsalt.community.bookmark.domain.BookmarkReqDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkResDto;
import com.example.savingsalt.community.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bookmark", description = "APIs for managing bookmarks on posts")
@RestController
@RequestMapping("/api/boards/tips")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @Operation(summary = "Bookmark or Unbookmark a Post", description = "Toggle bookmark status on a post for a member")
    @PostMapping("/{boardId}/book")
    public ResponseEntity<Void> bookmarkPost(@RequestBody BookmarkReqDto bookmarkReqDto) {
        bookmarkService.bookmarkBoard(bookmarkReqDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Count Bookmarks on a Post", description = "Get the total number of bookmarks for a specific post")
    @GetMapping("/{boardId}/bookmarks")
    public ResponseEntity<BookmarkResDto> countBookmarks(@PathVariable Long boardId) {
        BookmarkResDto response = bookmarkService.countBookmarks(boardId);
        return ResponseEntity.ok(response);
    }
}
