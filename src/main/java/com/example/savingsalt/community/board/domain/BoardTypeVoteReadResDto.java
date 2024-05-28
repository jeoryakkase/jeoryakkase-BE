package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.global.BaseEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeVoteReadResDto extends BaseEntity {

    private Long id;
    private String nickname;
    private String title;
    private String contents;
    private int boardHits;
    private PollResDto pollResData;

    public LocalDateTime getCreatedAt() {
        return this.getCreatedAt();
    }

    public LocalDateTime getModifiedAt() {
        return this.getModifiedAt();
    }

}
