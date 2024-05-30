package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.like.domain.LikeReqDto;
import com.example.savingsalt.community.like.domain.LikeResDto;

public interface MemberLikeService {
    void likeBoard(LikeReqDto likeReqDto);
    LikeResDto countLikes(Long boardId);
}
