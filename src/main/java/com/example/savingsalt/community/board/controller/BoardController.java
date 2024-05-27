package com.example.savingsalt.community.board.controller;

import com.example.savingsalt.community.board.domain.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 절약팁 게시글 생성
    @PostMapping("/boards/tips")
    public ResponseEntity<BoardTypeTipReadResDto> createTipBoard(
        @RequestBody BoardTypeTipCreateReqDto requestDto) {
        BoardTypeTipReadResDto responseDto = boardService.createTipBoard(requestDto);
        return ResponseEntity.ok(responseDto);
    }


}
