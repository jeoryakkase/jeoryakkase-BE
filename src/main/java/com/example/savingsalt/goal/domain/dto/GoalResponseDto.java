package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.member.domain.MemberEntity;
import java.time.LocalDate;
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
public class GoalResponseDto {

    private Long id;
    private MemberEntity memberEntity;
    private String goalTitle;
    private Long goalAmount;
    private Long currentAmount;
    private String goalImage;
    private LocalDate goalStartDate;
    private LocalDate goalEndDate;
    private GoalStatus goalStatus;

    public static GoalResponseDto fromEntity(GoalEntity goalEntity, MemberEntity memberEntity) {
        return GoalResponseDto.builder()
            .id(goalEntity.getId())
            .memberEntity(memberEntity)
            .goalTitle(goalEntity.getGoalTitle())
            .goalAmount(goalEntity.getGoalAmount())
            .goalImage(goalEntity.getGoalImage())
            .goalStartDate(goalEntity.getGoalStartDate())
            .goalEndDate(goalEntity.getGoalEndDate())
            .goalStatus(goalEntity.getGoalStatus())
            .build();
    }
}