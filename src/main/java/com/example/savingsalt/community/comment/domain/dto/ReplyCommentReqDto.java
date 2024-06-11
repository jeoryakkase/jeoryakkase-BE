package com.example.savingsalt.community.comment.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReplyCommentReqDto {

    private String content;
    private Long parentCommentId;

    public ReplyCommentReqDto(String content, Long parentCommentId) {
        this.content = content;
        this.parentCommentId = parentCommentId;
    }
}
