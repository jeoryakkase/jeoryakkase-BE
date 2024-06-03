package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import lombok.Getter;

@Getter
public class CommentReqDto {
    private Long boardId;
    private String comment;
}
