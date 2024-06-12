package com.example.savingsalt.community.like.service;

public interface MemberLikeService {
    String likePost(String email, Long boardId);
    int countLikes(Long boardId);
}
