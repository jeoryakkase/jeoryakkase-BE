package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.global.BaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResDto extends BaseEntity implements Comparable<CommentResDto> {

    private Long id;
    private String comment;
    private String nickname;

    // 작성일자만 사용하도록 설정
    public LocalDateTime getCreatedAt() {
        return this.getCreatedAt();
    }

    @Override
    public int compareTo(CommentResDto other) {
        return other.getCreatedAt().compareTo(this.getCreatedAt());
    }


}
