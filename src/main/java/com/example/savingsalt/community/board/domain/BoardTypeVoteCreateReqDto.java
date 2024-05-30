package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeVoteCreateReqDto {

    private String title;
    private String contents;
    private Long categoryId;
    private PollCreateReqDto pollReqData; // 투표 데이터 포함


}
