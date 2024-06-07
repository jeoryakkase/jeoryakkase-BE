package com.example.savingsalt.community.board.controller;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.exception.BoardException.BoardNotFoundException;
import com.example.savingsalt.community.board.service.BoardService;
import com.example.savingsalt.global.UnauthorizedException;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Board", description = "게시판 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    // 절약팁 게시글 생성
    @Operation(summary = "팁 게시물 작성", description = "로그인된 사용자가 팁 게시판에 새로운 게시물을 작성합니다.")
    @PostMapping("/tips")
    public ResponseEntity<BoardTypeTipReadResDto> createTipBoard(
        @RequestBody BoardTypeTipCreateReqDto requestDto,
        @AuthenticationPrincipal MemberEntity member) {

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        BoardTypeTipReadResDto responseDto = boardService.createTipBoard(requestDto, member);
        return ResponseEntity.ok(responseDto);
    }

    // 절약팁 게시글 목록 조회
    @Operation(summary = "팁 게시판 목록 조회", description = "팁 게시판에 있는 모든 게시글을 최신순으로 조회합니다.")
    @GetMapping("/tips")
    public ResponseEntity<List<BoardTypeTipReadResDto>> getTipBoards() {
        List<BoardTypeTipReadResDto> allTipBoard = boardService.findAllTipBoard();
        if (allTipBoard.isEmpty()) {
            throw new BoardNotFoundException();
        }
        return ResponseEntity.ok(allTipBoard);
    }

    // 절약팁 게시글 조회
    @Operation(summary = "팁 게시물 조회", description = "게시글 ID를 통해 팁 게시판의 게시글을 조회합니다.")
    @GetMapping("/tips/{boardId}")
    public ResponseEntity<BoardTypeTipReadResDto> getTipBoardById(@PathVariable Long boardId) {
        BoardTypeTipReadResDto tipBoardById = boardService.findTipBoardById(boardId);

        return ResponseEntity.ok(tipBoardById);
    }

    // 절약팁 게시글 수정
    @Operation(summary = "팁 게시글 수정", description = "게시글 ID를 통해 팁 게시판의 게시글 제목, 내용 및 이미지를 수정합니다.")
    @PatchMapping("/tips/{boardId}")
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
    @Operation(summary = "팁 게시판 삭제", description = "게시글 ID를 통해 팁 게시판의 게시글을 삭제합니다.")
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
    @Operation(summary = "투표 게시글 생성", description = "로그인된 사용자가 투표 게시판에 새로운 게시물을 작성합니다.")
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
    @Operation(summary = "투표게시판 목록 조회", description = "투표 게시판에 있는 모든 게시글을 최신순으로 조회합니다.")
    @GetMapping("/votes")
    public ResponseEntity<List<BoardTypeVoteReadResDto>> getVoteBoards() {
        List<BoardTypeVoteReadResDto> allVoteBoard = boardService.findAllVoteBoard();

        return ResponseEntity.ok(allVoteBoard);
    }

    // 투표 게시글 조회
    @Operation(summary = "투표 게시글 조회", description = "게시글 ID를 통해 투표 게시판의 게시글을 조회합니다.")
    @GetMapping("/votes/{boardId}")
    public ResponseEntity<BoardTypeVoteReadResDto> getVoteBoardById(@PathVariable Long boardId) {

        BoardTypeVoteReadResDto VoteBoardById = boardService.findVoteBoardById(boardId);

        return ResponseEntity.ok(VoteBoardById);
    }

    // 투표 게시글 수정
    @Operation(summary = "투표 게시글 수정", description = "게시글 ID를 통해 투표 게시판의 게시글 제목, 내용 및 이미지를 수정합니다.")
    @PatchMapping("/votes/{boardId}")
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
    @Operation(summary = "투표 게시글 삭제", description = "게시글 ID를 통해 투표 게시판의 게시글을 삭제합니다.")
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
