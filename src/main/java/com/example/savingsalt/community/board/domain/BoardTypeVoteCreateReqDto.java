package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.poll.domain.Poll;
import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.member.domain.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeVoteCreateReqDto {

    private String title;
    private String contents;
    private BoardCategory category;
    private PollCreateReqDto pollReqDto; // 투표 데이터 포함

    public BoardEntity toEntity(MemberEntity memberEntity, BoardCategory category) {
        return BoardEntity.builder()
            .memberEntity(memberEntity)
            .title(this.title)
            .contents(this.contents)
            .category(category)
            .build();
    }

    public Poll toPollEntity(BoardEntity boardEntity) {
        return pollReqDto.toEntity(boardEntity);
    }


}
