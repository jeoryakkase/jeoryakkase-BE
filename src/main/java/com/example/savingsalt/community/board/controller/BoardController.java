package com.example.savingsalt.community.board.controller;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteUpdateReqDto;
import com.example.savingsalt.community.board.exception.BoardException.EmptyBoardException;
import com.example.savingsalt.community.board.service.BoardService;
import com.example.savingsalt.global.UnauthorizedException;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Board", description = "게시판 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    private final MemberService memberService;

    private final MemberMapper memberMapper;

    @Operation(summary = "팁 게시글 작성", description = "로그인된 사용자가 팁 게시판에 새로운 게시글을 작성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = BoardTypeTipReadResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<BoardTypeTipReadResDto> createTipBoard(
        @RequestBody BoardTypeTipCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(memberService.findMemberByEmail(userDetails.getUsername()));

        BoardTypeTipReadResDto responseDto = boardService.createTipBoard(requestDto, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "팁 게시판 목록 조회", description = "팁 게시판에 있는 모든 게시글을 최신순으로 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BoardTypeTipReadResDto.class))),
        @ApiResponse(responseCode = "404", description = "게시글 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/tips")
    public ResponseEntity<Page<BoardTypeTipReadResDto>> getAllTipBoards(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<BoardTypeTipReadResDto> allTipBoard = boardService.findAllTipBoard(page, size);
        if (allTipBoard.isEmpty()) {
            throw new EmptyBoardException();
        }
        return ResponseEntity.ok(allTipBoard);
    }

    @Operation(summary = "팁 게시글 조회", description = "게시글 ID를 통해 팁 게시판의 게시글을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BoardTypeTipReadResDto.class))),
        @ApiResponse(responseCode = "404", description = "게시글 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoardTypeTipReadResDto> getTipBoardById(@PathVariable("id") Long id) {
        BoardTypeTipReadResDto tipBoardById = boardService.findTipBoardById(id);

        return ResponseEntity.ok(tipBoardById);
    }

    @Operation(summary = "팁 게시글 수정", description = "게시글 ID를 통해 팁 게시판의 게시글 제목, 내용 및 이미지를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = BoardTypeTipReadResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BoardTypeTipReadResDto> updateTipBoard(@PathVariable("id") Long id,
        @RequestBody BoardTypeTipCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(memberService.findMemberByEmail(userDetails.getUsername()));

        BoardTypeTipReadResDto responseDto = boardService.updateTipBoard(id, requestDto,
            member);
        return ResponseEntity.ok(responseDto);

    }

    @Operation(summary = "팁 게시글 삭제", description = "게시글 ID를 통해 팁 게시판의 게시글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTipBoard(@PathVariable("id") Long id,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(memberService.findMemberByEmail(userDetails.getUsername()));

        boardService.deleteTipBoard(id, member);
        return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);

    }

    @Operation(summary = "투표 게시글 작성", description = "로그인된 사용자가 투표 게시판에 새로운 게시글을 작성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "투표 게시글 작성 성공", content = @Content(schema = @Schema(implementation = BoardTypeVoteReadResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/votes")
    public ResponseEntity<BoardTypeVoteReadResDto> createVoteBoard(
        @RequestBody BoardTypeVoteCreateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(memberService.findMemberByEmail(userDetails.getUsername()));

        BoardTypeVoteReadResDto responseDto = boardService.createVoteBoard(requestDto, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "투표게시판 목록 조회", description = "투표 게시판에 있는 모든 게시글을 최신순으로 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BoardTypeVoteReadResDto.class))),
        @ApiResponse(responseCode = "404", description = "게시글 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/votes")
    public ResponseEntity<Page<BoardTypeVoteReadResDto>> getAllVoteBoards(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size) {
        Page<BoardTypeVoteReadResDto> allVoteBoard = boardService.findAllVoteBoard(page, size);

        if (allVoteBoard.isEmpty()) {
            throw new EmptyBoardException();
        }

        return ResponseEntity.ok(allVoteBoard);
    }

    @Operation(summary = "투표 게시글 조회", description = "게시글 ID를 통해 투표 게시판의 게시글을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BoardTypeVoteReadResDto.class))),
        @ApiResponse(responseCode = "404", description = "게시글 찾을 수 없음")
    })
    @GetMapping("/{id}/votes")
    public ResponseEntity<BoardTypeVoteReadResDto> getVoteBoardById(@PathVariable("id") Long id) {

        BoardTypeVoteReadResDto VoteBoardById = boardService.findVoteBoardById(id);

        return ResponseEntity.ok(VoteBoardById);
    }

    @Operation(summary = "투표 게시글 수정", description = "게시글 ID를 통해 투표 게시판의 게시글 제목, 내용 및 이미지를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = BoardTypeVoteReadResDto.class))),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("/{id}/votes")
    public ResponseEntity<?> updateVoteBoard(@PathVariable("id") Long id,
        @RequestBody BoardTypeVoteUpdateReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(memberService.findMemberByEmail(userDetails.getUsername()));

        BoardTypeVoteReadResDto responseDto = boardService.updateVoteBoard(id,
            requestDto, member);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "투표 게시글 삭제", description = "게시글 ID를 통해 투표 게시판의 게시글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{id}/votes")
    public ResponseEntity<String> deleteVoteBoard(@PathVariable("id") Long id,
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MemberEntity member = memberMapper.toEntity(memberService.findMemberByEmail(userDetails.getUsername()));

        boardService.deleteVoteBoard(id, member);

        return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);

    }


}
