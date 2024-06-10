package com.example.savingsalt.community.poll.controller;

import com.example.savingsalt.community.poll.domain.PollCreateReqDto;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollVoteReqDto;
import com.example.savingsalt.community.poll.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "투표 생성", description = "특정 게시글에 대해 투표를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "투표 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<PollEntity> createPoll(@RequestBody PollCreateReqDto pollCreateReqDto) {
        PollEntity createdPoll = pollService.createPollForBoard(pollCreateReqDto.getBoardId(), pollCreateReqDto.getStartTime(), pollCreateReqDto.getEndTime());
        return new ResponseEntity<>(createdPoll, HttpStatus.CREATED);
    }

    @Operation(summary = "투표하기", description = "특정 투표에 대해 회원이 투표합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "투표 성공"),
        @ApiResponse(responseCode = "404", description = "투표 또는 회원을 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "사용자가 이미 투표함"),
        @ApiResponse(responseCode = "400", description = "투표가 활성 상태가 아님")
    })
    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Void> vote(@PathVariable Long pollId, @RequestBody PollVoteReqDto pollVoteReqDto) {
        pollService.vote(pollId, pollVoteReqDto.getMemberId(), pollVoteReqDto.getPollVoteChoice());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "투표 결과 조회", description = "특정 투표의 결과를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "투표를 찾을 수 없음")
    })
    @GetMapping("/{pollId}/results")
    public ResponseEntity<PollResultDto> getPollResults(@PathVariable Long pollId) {
        PollResultDto pollResultDto = pollService.getPollResults(pollId);
        return new ResponseEntity<>(pollResultDto, HttpStatus.OK);
    }
}
