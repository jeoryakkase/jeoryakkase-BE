package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardTypeTipReadResDto {

    private String nickname;
    private String title;
    private String contents;
    private List<CommentResDto> comments;
    private int totalLike;
    private int view;
    private String imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
