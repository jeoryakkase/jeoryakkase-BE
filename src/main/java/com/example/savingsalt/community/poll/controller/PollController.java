package com.example.savingsalt.community.poll.controller;

import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollDto;
import com.example.savingsalt.community.poll.domain.PollParticipateReqDto;
import com.example.savingsalt.community.poll.domain.PollReadReqDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@Tag(name = "Poll", description = "Poll API")
public class PollController {

    @Autowired
    private PollService pollService;

    @Operation(summary = "Create a new Poll", description = "Creates a new Poll associated with a vote ID")
    @PostMapping("/poll")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Poll created successfully"),
        @ApiResponse(responseCode = "500", description = "Failed to create poll")
    })
    public ResponseEntity<PollDto> createPoll(@RequestBody PollCreateReqDto pollCreateReqDto) {
        PollDto response = pollService.createPoll(pollCreateReqDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a Poll", description = "Deletes an existing Poll by ID")
    @DeleteMapping("/{boardId}/poll/{pollId}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Poll deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Poll not found")
    })
    public ResponseEntity<Void> deletePoll(@PathVariable Long boardId, @PathVariable Long pollId) {
        pollService.deletePoll(boardId, pollId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a Poll", description = "Gets an existing Poll by ID")
    @GetMapping("/{boardId}/poll/{pollId}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Poll retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Poll not found")
    })
    public ResponseEntity<PollDto> getPoll(@RequestBody PollReadReqDto pollReadReqDto) {
        PollDto response = pollService.getPoll(pollReadReqDto.getBoardId(),
            pollReadReqDto.getPollId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Participate in a Poll", description = "Participate in an existing Poll by choosing an option")
    @PostMapping("/{boardId}/poll/{pollId}/participate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Participation successful"),
        @ApiResponse(responseCode = "404", description = "Poll or choice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<PollResultDto> participateInPoll(@RequestBody PollParticipateReqDto pollParticipateReqDto) {
        PollResultDto response = pollService.participateInPoll(pollParticipateReqDto.getBoardId(), pollParticipateReqDto.getPollId(), pollParticipateReqDto.getChoice());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Poll Results", description = "Gets the results of a Poll")
    @GetMapping("/{voteId}/poll/{pollId}/result")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Results retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Poll not found")
    })
    public ResponseEntity<List<PollChoiceDto>> getPollResults(@PathVariable Long voteId, @PathVariable Long pollId) {
        List<PollChoiceDto> response = pollService.getPollResults(voteId, pollId);
        return ResponseEntity.ok(response);
    }
}
