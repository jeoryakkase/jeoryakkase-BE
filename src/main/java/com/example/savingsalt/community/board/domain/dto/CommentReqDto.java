package com.example.savingsalt.community.board.domain.dto;

import lombok.Getter;

@Getter
public class CommentReqDto {
    private Long boardId;
    private String comment;

}
