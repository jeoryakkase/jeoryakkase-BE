package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeTipReadResDto;


public interface BoardService {

    // 절약팁 게시판 (like, bookmark 기능)
    BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto);

    // dto 추가 작성 후 작업예정
//    // 허불허 게시판 (투표게시판)
//    BoardTypeVoteReadResDto createVoteBoard(BoardTypeVoteCreateReqDto requestDto);
//    // 소금모아태산 게시판 (챌린지 달성 정보 작성 게시판)
//    BoardTypeHofReadResDto createHofBoard(BoardTypeHofCreateReqDto requestDto);


}
