package com.example.savingsalt.community.poll.controller;

import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.VoteReqDto;
import com.example.savingsalt.community.poll.service.PollService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
@Tag(name = "Poll", description = "투표 API")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Void> vote(@PathVariable Long pollId, @RequestBody VoteReqDto voteReqDto) {
        pollService.vote(pollId, voteReqDto.getMemberId(), voteReqDto.getVoteChoice());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{pollId}/results")
    public ResponseEntity<PollResultDto> getPollResults(@PathVariable Long pollId) {
        PollResultDto pollResultDto = pollService.getPollResults(pollId);
        return new ResponseEntity<>(pollResultDto, HttpStatus.OK);
    }
}
