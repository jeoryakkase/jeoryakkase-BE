package com.example.savingsalt.community.poll.controller;

import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
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
    @PostMapping("/{voteId}/poll")
    public ResponseEntity<PollDto> createPoll(@PathVariable Long voteId, @RequestBody PollCreateReqDto pollCreateReqDto) {
        PollDto response = pollService.createPoll(voteId, pollCreateReqDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a Poll", description = "Deletes an existing Poll by ID")
    @DeleteMapping("/{voteId}/poll/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long voteId, @PathVariable Long pollId) {
        pollService.deletePoll(voteId, pollId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a Poll", description = "Gets an existing Poll by ID")
    @GetMapping("/{voteId}/poll/{pollId}")
    public ResponseEntity<PollDto> getPoll(@PathVariable Long voteId, @PathVariable Long pollId) {
        PollDto response = pollService.getPoll(voteId, pollId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Participate in a Poll", description = "Participate in an existing Poll by choosing an option")
    @PostMapping("/{voteId}/poll/{pollId}/participate")
    public ResponseEntity<PollResultDto> participateInPoll(@PathVariable Long voteId, @PathVariable Long pollId, @RequestBody PollChoiceDto choiceDto) {
        PollResultDto response = pollService.participateInPoll(voteId, pollId, choiceDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Poll Results", description = "Gets the results of a Poll")
    @GetMapping("/{voteId}/poll/{pollId}/result")
    public ResponseEntity<List<PollChoiceDto>> getPollResults(@PathVariable Long voteId, @PathVariable Long pollId) {
        List<PollChoiceDto> response = pollService.getPollResults(voteId, pollId);
        return ResponseEntity.ok(response);
    }
}
