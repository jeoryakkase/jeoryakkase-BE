package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.challenge.domain.dto.ChallengeAchievementDetailsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeHofReadResDto {

    private Long postId;
    private String title;
    private String contents;
    private String nickname;
    private int totalLike;
    private List<ChallengeAchievementDetailsDto> achievements;

}
