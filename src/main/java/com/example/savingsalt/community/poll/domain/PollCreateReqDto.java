package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollCreateReqDto {

    private Long boardId;
    private List<PollChoiceDto> choices;

    public PollEntity toEntity(BoardEntity boardEntity) {
        List<PollChoiceEntity> pollChoices = choices.stream()
            .map(choiceDto -> PollChoiceEntity.builder()
                .answer(choiceDto.getAnswer())
                .count(choiceDto.getCount())
                .build())
            .collect(Collectors.toList());

        // Poll 엔티티 생성 시 연관 관계 설정
        PollEntity poll = PollEntity.builder()
            .board(boardEntity)
            .choices(pollChoices)
            .build();

        return poll;
    }
}
