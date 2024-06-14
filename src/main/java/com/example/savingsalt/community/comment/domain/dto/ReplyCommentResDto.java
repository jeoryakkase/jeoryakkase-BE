package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReplyCommentResDto {
    private Long replyId;
    private String content;
    private BadgeDto badgeDto;
    private String nickname;

}
