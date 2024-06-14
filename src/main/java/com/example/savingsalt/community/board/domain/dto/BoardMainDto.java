package com.example.savingsalt.community.board.domain.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class BoardMainDto {

    private Long id;
    private String title;
    private String contents;
    private List<BoardImageDto> boardImageDtos;

}
