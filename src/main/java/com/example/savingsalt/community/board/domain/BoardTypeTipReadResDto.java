package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.global.BaseEntity;
import java.time.LocalDateTime;
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

    public LocalDateTime getCreatedAt() {
        return this.getCreatedAt();
    }

    public LocalDateTime getModifiedAt() {
        return this.getModifiedAt();
    }


}
