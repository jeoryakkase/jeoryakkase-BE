package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Schema(description = "Poll creation request")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollCreateReqDto {
    @Schema(description = "Board ID", example = "1")
    private Long boardId;
    @Schema(description = "List of poll choices", example = "[{\"answer\": \"Option 1\", \"count\": 0}, {\"answer\": \"Option 2\", \"count\": 0}]")
    private List<PollChoiceDto> choices;

    public PollEntity toEntity(BoardEntity boardEntity) {
        PollEntity poll = PollEntity.builder()
            .board(boardEntity)
            .build();

        List<PollChoiceEntity> pollChoices = choices.stream()
            .map(choiceDto -> PollChoiceEntity.builder()
                .answer(choiceDto.getAnswer())
                .count(choiceDto.getCount())
                .pollEntity(poll) // 연관 관계 설정
                .build())
            .collect(Collectors.toList());

        poll.setChoices(pollChoices);

        return poll;
    }
}
