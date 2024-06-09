package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.poll.enums.VoteChoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteReqDto {
    private Long memberId;
    private VoteChoice voteChoice;
}
