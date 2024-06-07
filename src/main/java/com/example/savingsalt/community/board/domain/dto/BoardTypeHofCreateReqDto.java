package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeHofCreateReqDto {

    private String contents;

    public BoardEntity toEntity(MemberEntity member) {

        return BoardEntity.builder()
            .memberEntity(member)
            .title("Challenge Completed!")
            .contents(contents)
            .category(BoardCategory.HALL_OF_FAME)
            .build();
    }

}
