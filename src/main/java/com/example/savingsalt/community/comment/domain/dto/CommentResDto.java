package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.community.comment.domain.CommentEntity;
import com.example.savingsalt.global.BaseEntity;
import lombok.Getter;

@Getter
public class CommentResDto extends BaseEntity implements Comparable<CommentResDto> {

    private Long id;
    private String comment;
    private String nickname;


    // id를 기준으로 내림차순 정렬
    @Override
    public int compareTo(CommentResDto other) {
        return other.getCreatedAt().compareTo(this.getCreatedAt());
    }

    public CommentResDto(CommentEntity comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getNickname();
    }




}
