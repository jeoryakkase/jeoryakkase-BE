package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.like.domain.MemberLikeDto;

public interface MemberLikeService {
    String likePost(MemberLikeDto memberLikeDto);
    int countLikes(Long boardId);
}
