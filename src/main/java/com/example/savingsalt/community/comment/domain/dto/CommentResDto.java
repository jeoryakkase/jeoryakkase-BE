package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentResDto {

    private Long commentId;
    private String content;
    private String nickname;
    private BadgeDto badgeDto;
    private List<ReplyCommentResDto> replyComments;

}
