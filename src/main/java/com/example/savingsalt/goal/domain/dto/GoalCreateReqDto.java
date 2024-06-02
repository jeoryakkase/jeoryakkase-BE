package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotEmpty(message = "목표 제목은 필수입력 사항입니다.")
    private String goalTitle;   // 목표 제목

    @NotNull(message = "목표 금액은 필수입력 사항입니다.")
    @Positive(message = "목표 금액은 양수만 입력할 수 있습니다.")
    private Long goalAmount;    // 목표 금액

    private String goalImage;   // 목표 이미지

    @NotNull(message = "목표 시작 일정은 필수입니다.")
    private LocalDateTime goalStartDate;    // 목표 시작 일정

    @NotNull(message = "목표 끝나는 일정은 필수입니다.")
    private LocalDateTime goalEndDate;      // 목표 끝나는 일정

    // toEntity 메서드 추가 => mapper 로 나중에 전환할 것
    public GoalEntity toEntity(GoalCreateReqDto goalCreateReqDto) {
        return GoalEntity.builder()
            .goalTitle(this.goalTitle)
            .goalAmount(this.goalAmount)
            .goalImage(this.goalImage)
            .goalStartDate(this.goalStartDate)
            .goalEndDate(this.goalEndDate)
            .build();
    }
}
