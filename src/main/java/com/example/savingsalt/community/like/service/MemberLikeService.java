package com.example.savingsalt.community.like.service;

import com.example.savingsalt.community.like.domain.LikeReqDto;
import com.example.savingsalt.community.like.domain.LikeResDto;
import com.example.savingsalt.community.like.domain.MemberLikeDto;

public interface MemberLikeService {
    MemberLikeDto likeBoard(LikeReqDto likeReqDto);
    LikeResDto countLikes(Long boardId);
}
