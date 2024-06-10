package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.time.LocalDateTime;
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
    private PollCreateReqDto pollReqDto; // 투표 데이터 포함
    private LocalDateTime startTime; // 투표 기간 설정 필드 추가
    private LocalDateTime endTime;

    public BoardEntity toEntity(MemberEntity member) {
        return BoardEntity.builder()
            .memberEntity(member)
            .title(title)
            .contents(contents)
            .build();
    }
/* no usage상태 + 에러 때문에 임의로 주석처리

    public PollEntity toPollEntity(BoardEntity boardEntity) {

        return pollReqDto.toEntity(boardEntity);
    }
*/

}
