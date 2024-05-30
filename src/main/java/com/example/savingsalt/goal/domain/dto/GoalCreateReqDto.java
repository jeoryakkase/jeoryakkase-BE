package com.example.savingsalt.goal.domain.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalCreateReqDto {

    private Long goalAmount;    // 목표 금액
    private String goalImage;   // 목표 이미지
    private LocalDateTime goalStartDate;    // 목표 시작 일정
    private LocalDateTime goalEndDate;      // 목표 끝나는 일정

}
