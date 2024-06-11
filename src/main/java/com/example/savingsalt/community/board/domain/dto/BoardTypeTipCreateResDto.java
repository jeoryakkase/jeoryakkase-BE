package com.example.savingsalt.community.board.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
public class BoardTypeTipCreateResDto {

    private String nickname;
    private String title;
    private String contents;
    private int totalLike;
    private int view;
    private String imageUrls;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

}
