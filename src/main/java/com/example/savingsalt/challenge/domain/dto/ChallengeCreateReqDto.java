package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeCreateReqDto {

    @NotBlank(message = "챌린지 제목은 필수입력 사항입니다.")
    @Size(min = 1, max = 50, message = "제목은 1 ~ 50자 이여야 합니다!")
    private String challengeTitle;

    @NotBlank(message = "챌린지 설명은 필수입력 사항입니다.")
    private String challengeDesc;

    private int challengeGoal;

    private int challengeCount;

    @NotBlank(message = "챌린지 유형은 필수입력 사항입니다.")
    @Pattern(regexp = "COUNT|GOAL", message = "챌린지 유형은 COUNT(횟수), GOAL(모으기) 중 하나여야 합니다.")
    private String challengeType;

    @NotBlank(message = "챌린지 기간은 필수입력 사항입니다.")
    @Size(min = 1, max = 50, message = "챌린지 기간은 1 ~ 20자 이여야 합니다!")
    private String challengeTerm;

    @NotBlank(message = "챌린지 난이도는 필수입력 사항입니다.")
    @Pattern(regexp = "UNSET|EASY|NORMAL|HARD", message = "챌린지 난이도는 UNSET, EASY, NORMAL, HARD 중 하나여야 합니다.")
    private String challengeDifficulty;

    @NotBlank(message = "챌린지 난이도는 필수입력 사항입니다.")
    private String authContent;

    private Long badgeId;

}
