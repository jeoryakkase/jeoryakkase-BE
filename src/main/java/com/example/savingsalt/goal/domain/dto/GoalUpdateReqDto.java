package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalUpdateReqDto {

    private Long goalAmount;
    private String goalImage;
    private String goalDesc;
    private LocalDateTime goalStartDate;
    private LocalDateTime goalEndDate;
    private Long currentAmount;
    private String goalStatus;

    public GoalEntity toEntity(Long id, GoalEntity existingGoalEntity) {
        return GoalEntity.builder()
            .id(id)
            .memberEntity(existingGoalEntity.getMemberEntity())
            .goalAmount(goalAmount)
            .goalImage(goalImage)
            .goalDesc(goalDesc)
            .goalStartDate(goalStartDate)
            .goalEndDate(goalEndDate)
            .currentAmount(currentAmount)
            .goalStatus(goalStatus)
            .build();
    }
}