package com.example.savingsalt.community.board.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeHofCreateReqDto {

    private String title;
    private String contents;
    private List<Long> challengeAchievementsIds; // // 사용자가 완료하고 자랑하기를 선택한 챌린지 ID들

}
