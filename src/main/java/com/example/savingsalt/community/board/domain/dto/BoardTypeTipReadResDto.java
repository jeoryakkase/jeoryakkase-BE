package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder(toBuilder = true)
@Getter
public class BoardTypeTipReadResDto {

    private Long id;
    private String nickname;
    private String profileImage;
    private BadgeDto badgeDto;
    private String title;
    private String contents;
    private List<CommentResDto> comments;
    private int totalLike;
    private int view;
    private List<BoardImageDto> boardImageDtos;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedAt;


}
