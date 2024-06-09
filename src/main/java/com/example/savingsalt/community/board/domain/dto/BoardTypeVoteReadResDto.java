package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.global.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
public class BoardTypeVoteReadResDto extends BaseEntity {

    private Long id;
    private String nickname;
    private String title;
    private String contents;
    private List<CommentResDto> comments;
    private int view;
    private PollResDto pollResDto;


}
