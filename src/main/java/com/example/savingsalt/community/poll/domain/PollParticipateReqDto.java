package com.example.savingsalt.community.poll.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollParticipateReqDto {
    private Long boardId;
    private Long pollId;
    private PollChoiceDto choice;
}
