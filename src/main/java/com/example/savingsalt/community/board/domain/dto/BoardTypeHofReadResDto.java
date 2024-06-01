package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.challenge.domain.dto.ChallengeAchievementDetailsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class BoardTypeHofReadResDto {

    private Long id;
    private String title;
    private String contents;
    private String nickname;
    private int totalLike;
    private List<ChallengeAchievementDetailsDto> achievements;

}
