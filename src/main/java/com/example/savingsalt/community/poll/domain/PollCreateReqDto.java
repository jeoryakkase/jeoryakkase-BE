package com.example.savingsalt.community.poll.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollCreateReqDto {
    private Long boardId;
    private List<PollChoiceDto> choices;
}
