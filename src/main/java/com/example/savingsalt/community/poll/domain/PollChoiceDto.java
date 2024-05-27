package com.example.savingsalt.community.poll.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollChoiceDto {
    private Long id;
    private String answer;
    private int count;
}
