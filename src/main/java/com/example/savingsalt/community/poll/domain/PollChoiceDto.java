package com.example.savingsalt.community.poll.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Schema(description = "Poll choice details")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollChoiceDto {
    @Schema(description = "Choice ID", example = "1")
    private Long id;
    @Schema(description = "Choice answer", example = "Option 1")
    private String answer;
    @Schema(description = "Choice count", example = "0")
    private int count;
}
