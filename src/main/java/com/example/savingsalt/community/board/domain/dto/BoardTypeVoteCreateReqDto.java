package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardTypeVoteCreateReqDto {

    private String title;
    private String contents;
    private BoardCategory category = BoardCategory.VOTE;

    public BoardEntity toEntity(MemberEntity member) {
        return BoardEntity.builder()
            .memberEntity(member)
            .title(title)
            .contents(contents)
            .category(category)
            .build();
    }
}
