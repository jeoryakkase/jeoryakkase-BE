package com.example.savingsalt.goal.domain.dto;

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
public class GoalMainResponseDto {

    // 목표 고유 ID
    private Long goalId;

    // 멤버 닉네임
    private String memberNickName;

    // 목표 제목
    private String goalTitle;

    // 묙표 남은 금액
    private Long goalRemainingAmount;

    // 목표 달성률
    private Long goalAchievementRate;

    // 진행 일자
    private Long goalDateOfProgress;

    // 남은 일자
    private Long goalRemainingPeriod;
}
