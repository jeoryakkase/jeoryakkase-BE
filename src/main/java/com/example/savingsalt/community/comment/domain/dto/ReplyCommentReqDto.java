package com.example.savingsalt.community.comment.domain.dto;

import lombok.Getter;

@Getter
public class ReplyCommentReqDto {

    private String content;
    private Long parentCommentId;
    private Long memberId;

    public ReplyCommentReqDto(String content, Long parentCommentId, Long memberId) {
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.memberId = memberId;
    }
}
