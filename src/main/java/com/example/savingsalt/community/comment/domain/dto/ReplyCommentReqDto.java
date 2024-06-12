package com.example.savingsalt.community.comment.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReplyCommentReqDto {

    private Long boardId;
    private String content;
    private Long parentCommentId;

}
