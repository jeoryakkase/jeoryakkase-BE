package com.example.savingsalt.community.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentReqDto {

    private Long boardId;
    private Long memberId;
    private String content;


}
