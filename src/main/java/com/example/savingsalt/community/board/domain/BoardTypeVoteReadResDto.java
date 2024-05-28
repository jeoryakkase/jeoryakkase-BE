package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.poll.domain.PollResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeVoteReadResDto {

    private Long id;
    private String nickname;
    private String title;
    private String contents;
    private int boardHits;
    private PollResDto pollResData;

}
