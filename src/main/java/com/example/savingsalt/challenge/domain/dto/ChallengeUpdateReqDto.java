package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.challenge.domain.entity.ChallengeEntity.ChallengeDifficulty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeUpdateReqDto {

    @Size(min = 1, max = 50, message = "제목은 1 ~ 50자 이여야 합니다!")
    private String challengeTitle;

    private String challengeDesc;

    private Integer challengeGoal;

    private Integer challengeCount;

    @Size(min = 1, max = 50, message = "챌린지 유형은 1 ~ 20자 이여야 합니다!")
    private String challengeType;

    @Size(min = 1, max = 50, message = "챌린지 기간은 1 ~ 20자 이여야 합니다!")
    private String challengeTerm;

    @Pattern(regexp = "UNSET|EASY|NORMAL|HARD", message = "챌린지 난이도는 UNSET, EASY, NORMAL, HARD 중 하나여야 합니다.")
    private ChallengeDifficulty challengeDifficulty;

    private LocalDateTime updateAt;

    private String authContent;

    private Long badgeId;
}
