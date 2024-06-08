package com.example.savingsalt.community.poll.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PollReadReqDto {
    private Long boardId;
    private Long pollId;
}
