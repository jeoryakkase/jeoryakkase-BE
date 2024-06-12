package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardTypeVoteUpdateReqDto {

    private String title;
    private String contents;
    private BoardCategory category = BoardCategory.VOTE;

}
