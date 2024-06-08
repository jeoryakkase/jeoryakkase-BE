package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.global.BaseEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReplyCommentResDto extends BaseEntity {
    private Long id;
    private String content;
    private Long parentCommentId;
    private Long memberId;
    private String nickname;

}
