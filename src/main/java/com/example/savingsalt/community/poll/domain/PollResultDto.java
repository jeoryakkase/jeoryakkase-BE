package com.example.savingsalt.community.poll.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollResultDto {
    private Long id;
    private Long pollId;
    private Long choiceId;
    private Long memberId;
}
