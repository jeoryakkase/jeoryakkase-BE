package com.example.savingsalt.community.board.controller;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.service.BoardService;
import com.example.savingsalt.member.domain.MemberEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Board", description = "Board API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    // 절약팁 게시글 생성
    @Operation(summary = "Create a new TipBoard", description = "create a new post in the Tip Board")
    @PostMapping("/tips")
    public ResponseEntity<?> createTipBoard(
        @RequestBody BoardTypeTipCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        MemberEntity member = (MemberEntity) userDetails;

        try {
            BoardTypeTipReadResDto responseDto = boardService.createTipBoard(requestDto, member);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 절약팁 게시글 목록 조회
    @Operation(summary = "Get all Tip Boards", description = "Retrieve a list of all posts available in the Tip Board, sorted by the most recent.")
    @GetMapping("/tips")
    public ResponseEntity<List<BoardTypeTipReadResDto>> getTipBoards() {
        List<BoardTypeTipReadResDto> allTipBoard = boardService.findAllTipBoard();

        return ResponseEntity.ok(allTipBoard);
    }

    // 절약팁 게시글 조회
    @Operation(summary = "Get a Tip Board", description = "Retrieve the details of a specific post in the Tip Board using its ID.")
    @GetMapping("/tips/{boardId}")
    public ResponseEntity<BoardTypeTipReadResDto> getTipBoardById(@PathVariable Long boardId) {
        BoardTypeTipReadResDto tipBoardById = boardService.findTipBoardById(boardId);
        return ResponseEntity.ok(tipBoardById);
    }

    // 절약팁 게시글 수정
    @Operation(summary = "update a Tip Board", description = "Modify the title, contents, and images of a specific post in the Tip Board by its ID.")
    @PutMapping("/tips/{boardId}")
    public ResponseEntity<?> updateTipBoard(@PathVariable Long boardId,
        @RequestBody BoardTypeTipCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        MemberEntity member = (MemberEntity) userDetails;

        try {
            BoardTypeTipReadResDto responseDto = boardService.updateTipBoard(boardId,
                requestDto, member);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // 절약팁 게시글 삭제
    @Operation(summary = "Delete a Tip Board", description = "Remove a specific post from the Tip Board by its ID.")
    @DeleteMapping("/tips/{boardId}")
    public ResponseEntity<String> deleteTipBoard(@PathVariable Long boardId,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        MemberEntity member = (MemberEntity) userDetails;

        try {
            boardService.deleteTipBoard(boardId, member);
            return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    // 투표 게시글 작성
    @Operation(summary = "Create a Vote Board", description = "create a new post in the Vote Board")
    @PostMapping("/votes")
    public ResponseEntity<?> createVoteBoard(@RequestBody BoardTypeVoteCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        MemberEntity member = (MemberEntity) userDetails;

        BoardTypeVoteReadResDto responseDto = boardService.createVoteBoard(requestDto, member);

        return ResponseEntity.ok(responseDto);
    }

    // 투표 게시글 목록 조회
    @Operation(summary = "Get all Vote Boards", description = "Retrieve a list of all posts available in the Vote Board, sorted by the most recent.")
    @GetMapping("/votes")
    public ResponseEntity<List<BoardTypeVoteReadResDto>> getVoteBoards() {
        List<BoardTypeVoteReadResDto> allVoteBoard = boardService.findAllVoteBoard();

        return ResponseEntity.ok(allVoteBoard);
    }

    // 투표 게시글 조회
    @Operation(summary = "Get a Vote Board", description = "Retrieve the details of a specific post in the Vote Board using its ID.")
    @GetMapping("/votes/{boardId}")
    public ResponseEntity<BoardTypeVoteReadResDto> getVoteBoardById(@PathVariable Long boardId) {

        BoardTypeVoteReadResDto VoteBoardById = boardService.findVoteBoardById(boardId);

        return ResponseEntity.ok(VoteBoardById);
    }

    // 투표 게시글 수정
    @Operation(summary = "update a Vote Board", description = "Modify the title, contents, and images of a specific post in the Vote Board by its ID.")
    @PutMapping("/votes/{boardId}")
    public ResponseEntity<?> updateVoteBoard(@PathVariable Long boardId,
        @RequestBody BoardTypeTipCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        MemberEntity member = (MemberEntity) userDetails;

        BoardTypeTipReadResDto responseDto = boardService.updateTipBoard(boardId,
            requestDto, member);

        return ResponseEntity.ok(responseDto);
    }

    // 투표 게시글 삭제
    @Operation(summary = "Delete a Vote Board", description = "Remove a specific post from the Vote Board by its ID.")
    @DeleteMapping("/votes/{boardId}")
    public ResponseEntity<String> deleteVoteBoard(@PathVariable Long boardId,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        MemberEntity member = (MemberEntity) userDetails;

        boardService.deleteTipBoard(boardId, member);

        return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);

    }



}
