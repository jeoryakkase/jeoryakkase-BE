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
public class GoalCertificationStatisticsResDto {

    // 일일 인증 금액
    private Long dailyAmount;

    // 월간 인증 금액
    private Long monthlyAmount;

    // 총 인증 금액
    private Long totalAmount;
}
