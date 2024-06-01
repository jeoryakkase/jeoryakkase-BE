package com.example.savingsalt.community.like.controller;

import com.example.savingsalt.community.like.domain.LikeReqDto;
import com.example.savingsalt.community.like.domain.LikeResDto;
import com.example.savingsalt.community.like.domain.MemberLikeDto;
import com.example.savingsalt.community.like.service.MemberLikeService;
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

@Tag(name = "Member Like", description = "APIs for managing likes on posts")
@RestController
@RequestMapping("/api/boards/tips")
public class MemberLikeController {

    @Autowired
    private MemberLikeService memberLikeService;

    @Operation(summary = "Like or Unlike a Post", description = "Toggle like status on a post for a member")
    @PostMapping("/like")
    public ResponseEntity<MemberLikeDto> likePost(@RequestBody LikeReqDto likeReqDto) {
        MemberLikeDto response = memberLikeService.likeBoard(likeReqDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Count Likes on a Post", description = "Get the total number of likes for a specific post")
    @GetMapping("/{boardId}/likes")
    public ResponseEntity<LikeResDto> countLikes(@PathVariable Long boardId) {
        LikeResDto response = memberLikeService.countLikes(boardId);
        return ResponseEntity.ok(response);
    }
}
