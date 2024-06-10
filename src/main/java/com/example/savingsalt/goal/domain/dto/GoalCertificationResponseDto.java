package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalCertificationResponseDto {

    private Long id;
    private Long goalId;
    private Long certificationMoney;
    private String certificationContent;
    private String certificationImageUrl;
    private LocalDate certificationDate;

    // fromEntity 메소드
    public static GoalCertificationResponseDto fromEntity(GoalCertificationEntity entity) {
        return GoalCertificationResponseDto.builder()
            .id(entity.getId())
            .goalId(entity.getGoalEntity().getId())
            .certificationMoney(entity.getCertificationMoney())
            .certificationContent(entity.getCertificationContent())
            .certificationImageUrl(entity.getCertificationImageUrl())
            .certificationDate(entity.getCertificationDate())
            .build();
    }
}
