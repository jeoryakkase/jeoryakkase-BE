package com.example.savingsalt.goal.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
public class GoalCategoryCreateReqDto {

    @NotEmpty(message = "인증 내용은 필수 입력 사항입니다.")
    @Schema(description = "인증 내용", example = "대중교통 이용하기", required = true)
    private String certificationDetails;
}