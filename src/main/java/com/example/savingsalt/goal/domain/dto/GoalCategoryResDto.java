package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalCategoryEntity;
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
public class GoalCategoryResDto {

    private Long id;
    private String certificationDetails;

    public static GoalCategoryResDto fromEntity(GoalCategoryEntity entity) {
        return GoalCategoryResDto.builder()
            .id(entity.getId())
            .certificationDetails(entity.getCertificationDetails())
            .build();
    }
}
