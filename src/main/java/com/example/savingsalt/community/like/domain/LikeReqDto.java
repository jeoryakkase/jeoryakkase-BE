package com.example.savingsalt.community.like.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeReqDto {
    private Long boardId;
    private Long memberId;
}