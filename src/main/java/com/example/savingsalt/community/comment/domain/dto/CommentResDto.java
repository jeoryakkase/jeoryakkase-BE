package com.example.savingsalt.community.comment.domain.dto;

import java.time.LocalDateTime;
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

    private Long id;
    private String content;
    private String nickname;
    private List<CommentResDto> replyComments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int depth;
    private Long level;

}
