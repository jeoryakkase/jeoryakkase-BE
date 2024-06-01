package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.global.BaseEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
public class BoardTypeVoteReadResDto extends BaseEntity {

    private Long id;
    private String nickname;
    private String title;
    private String contents;
    private int boardHits;
    private PollResDto pollResDto;


}
