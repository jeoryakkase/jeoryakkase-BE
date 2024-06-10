package com.example.savingsalt.community.poll.domain;

import java.time.LocalDateTime;
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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
