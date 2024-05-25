package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.global.BaseEntity;
import lombok.Getter;

@Getter
public class BoardTypeTipReadResDto extends BaseEntity {

    private String nickname;
    private String title;
    private String contents;
    private int totalLike;
    private int boardHits;


}
