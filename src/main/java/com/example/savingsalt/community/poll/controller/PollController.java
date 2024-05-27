package com.example.savingsalt.community.poll.controller;

import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote/{voteId}/poll")
@Tag(name = "Poll", description = "Poll API")
public class PollController {

    @Autowired
    private PollService pollService;

    @Operation(summary = "Create a new Poll", description = "Creates a new Poll associated with a vote ID")
    @PostMapping
    public PollResDto createPoll(@Parameter(description = "ID of the vote to which the poll belongs") @PathVariable Long voteId, @RequestBody PollCreateReqDto pollCreateReqDto) {
        return pollService.createPoll(voteId, pollCreateReqDto);
    }

    @Operation(summary = "Delete a Poll", description = "Deletes an existing Poll by ID")
    @DeleteMapping("/{pollId}")
    public void deletePoll(@Parameter(description = "ID of the vote to which the poll belongs") @PathVariable Long voteId, @Parameter(description = "ID of the poll to delete") @PathVariable Long pollId) {
        pollService.deletePoll(voteId, pollId);
    }

    @Operation(summary = "Get a Poll", description = "Gets an existing Poll by ID")
    @GetMapping("/{pollId}")
    public PollResDto getPoll(@Parameter(description = "ID of the vote to which the poll belongs") @PathVariable Long voteId, @Parameter(description = "ID of the poll to get") @PathVariable Long pollId) {
        return pollService.getPoll(voteId, pollId);
    }

    @Operation(summary = "Participate in a Poll", description = "Participate in an existing Poll by choosing an option")
    @PostMapping("/{pollId}/participate")
    public PollResultDto participateInPoll(@Parameter(description = "ID of the vote to which the poll belongs") @PathVariable Long voteId, @Parameter(description = "ID of the poll to participate in") @PathVariable Long pollId, @RequestBody PollChoiceDto choiceDto) {
        return pollService.participateInPoll(voteId, pollId, choiceDto);
    }

    @Operation(summary = "Get Poll Results", description = "Gets the results of a Poll")
    @GetMapping("/{pollId}/result")
    public List<PollResultDto> getPollResults(@Parameter(description = "ID of the vote to which the poll belongs") @PathVariable Long voteId, @Parameter(description = "ID of the poll to get results for") @PathVariable Long pollId) {
        return pollService.getPollResults(voteId, pollId);
    }
}
