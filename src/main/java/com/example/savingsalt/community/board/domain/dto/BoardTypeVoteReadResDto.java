package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@AllArgsConstructor
public class BoardTypeVoteReadResDto{

    private Long id;
    private String nickname;
    private String title;
    private String contents;
    private List<CommentResDto> comments;
    private int view;
    private int totalLike;
    private List<BoardImageDto> boardImageDtos;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedAt;
    private PollResultDto pollResultDto;

}
