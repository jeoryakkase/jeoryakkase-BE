package com.example.savingsalt.community.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReplyCommentResDto {
    private Long id;
    private String content;
    private String nickname;

}
