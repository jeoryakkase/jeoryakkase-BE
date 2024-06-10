package com.example.savingsalt.goal.domain.dto;

import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class GoalCertificationCreateReqDto {

    private String certificationImageUrl;

    @NotNull(message = "인증 금액은 필수 입력 사항입니다.")
    @Positive(message = "인증 금액은 양수여야 합니다.")
    @Schema(description = "인증 금액", example = "10000", required = true)
    private Long certificationMoney;

    @NotEmpty(message = "인증 내용은 선택은 필수 사항입니다.")
    @Schema(description = "인증 내용", example = "대중교통 이용하기", required = true)
    private String certificationContent;


    public GoalCertificationEntity toEntity(
        GoalCertificationCreateReqDto goalCertificationCreateReqDto, GoalEntity goalEntity,
        MemberEntity memberEntity) {
        return GoalCertificationEntity.builder()
            .goalEntity(goalEntity)
            .memberEntity(memberEntity)
            .certificationMoney(goalCertificationCreateReqDto.getCertificationMoney())
            .certificationContent(goalCertificationCreateReqDto.getCertificationContent())
            .certificationImageUrl(goalCertificationCreateReqDto.getCertificationImageUrl())
            .certificationDate(LocalDate.now())
            .build();
    }
}
