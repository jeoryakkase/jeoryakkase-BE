package com.example.savingsalt.challenge.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeAchievementDetailsDto {

    private String challengeTitle;
    private LocalDateTime certifyDate;
    private String challengeComment;
    private Integer challengeConut;
    private Integer saveMoney;


}
