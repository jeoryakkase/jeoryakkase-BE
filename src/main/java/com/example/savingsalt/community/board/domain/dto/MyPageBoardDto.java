package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageBoardDto {

    private Long id;
    private Long memberId;
    private String title;
    private String contents;
    private int totalLike;
    private int view;
    private BoardCategory category;
    private String imageUrls;
}
