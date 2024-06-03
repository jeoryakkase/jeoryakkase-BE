package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.global.BaseEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardTypeTipReadResDto extends BaseEntity {

    private Long id;
    private String nickname;
    private String title;
    private String contents;
    private int totalLike;
    private int boardHits;


}
