package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.global.BaseEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentResDto extends BaseEntity implements Comparable<CommentResDto> {

    private Long id;
    private String content;
    private String nickname;
    private List<ReplyCommentResDto> replyComments;


    // id를 기준으로 내림차순 정렬
    @Override
    public int compareTo(CommentResDto other) {
        return other.getCreatedAt().compareTo(this.getCreatedAt());
    }
}
