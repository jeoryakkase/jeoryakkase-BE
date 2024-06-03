package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.challenge.domain.dto.ChallengeAchievementDetailsDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class BoardTypeHofReadResDto {

    private Long id;
    private String nickname;
    private String contents;
    private int totalLike;
    private List<ChallengeAchievementDetailsDto> achievements;

    public BoardEntity toEntity(MemberEntity member) {
        return BoardEntity.builder()
            .memberEntity(member)
            .nickname(nickname)
            .contents(contents)
            .totalLike(totalLike)
            .build();
    }



}
