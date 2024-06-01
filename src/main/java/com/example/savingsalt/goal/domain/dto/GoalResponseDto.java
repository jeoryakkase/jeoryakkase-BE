package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponseDto {

    private Long id;
    private Long goalAmount;
    private String goalImage;
    private LocalDateTime goalStartDate;
    private LocalDateTime goalEndDate;

    public static GoalResponseDto fromEntity(GoalEntity goalEntity) {
        return GoalResponseDto.builder()
            .id(goalEntity.getId())
            .goalAmount(goalEntity.getGoalAmount())
            .goalImage(goalEntity.getGoalImage())
            .goalStartDate(goalEntity.getGoalStartDate())
            .goalEndDate(goalEntity.getGoalEndDate())
            .build();
    }
}