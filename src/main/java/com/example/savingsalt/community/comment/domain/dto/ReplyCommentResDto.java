package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.global.BaseEntity;

public class ReplyCommentResDto extends BaseEntity {
    private Long id;
    private String comment;
    private Long parentCommentId;
    private Long memberId;

}
