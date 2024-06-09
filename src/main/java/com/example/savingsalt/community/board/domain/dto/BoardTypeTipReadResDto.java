package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.global.BaseEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardTypeTipReadResDto extends BaseEntity {

    private String nickname;
    private String title;
    private String contents;
    private List<CommentResDto> comments;
    private int totalLike;
    private int view;


}
