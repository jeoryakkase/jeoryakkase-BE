package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.poll.enums.PollVoteChoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollVoteReqDto {
    private Long memberId;
    private PollVoteChoice pollVoteChoice;
}
