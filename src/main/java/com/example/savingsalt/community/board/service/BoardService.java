package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeTipReadResDto;


public interface BoardService {

    // 절약팁 게시판 (like, bookmark 기능)

    // 절약팁 게시글 작성
    BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto);

    // 절약팁 게시글 목록 조회
//    List<BoardTypeTipReadResDto> findAllTipBoard();
//    // 절약팁 게시글 조회
//    BoardTypeTipReadResDto findTipBoardById(Long id);
//    // 절약팁 게시글 수정
//    BoardTypeTipReadResDto updateTipBoard(Long id, BoardTypeTipCreateReqDto boardDto);
//    // 절약팁 게시글 삭제
//    void deleteTipBoard(Long id);
//
//    // 허불허 게시판 (투표게시판)
//
//    // 투표 게시글 작성
//    BoardTypeVoteReadResDto createVoteBoard(BoardTypeVoteCreateReqDto requestDto);
//
//    // 투표 게시글 목록 조회
//    List<BoardTypeVoteReadResDto> findAllVoteBoard();
//
//    // 투표 게시글 조회
//    BoardTypeVoteReadResDto findVoteBoardById(Long id);
//
//    // 투표 게시글 수정
//    BoardTypeVoteReadResDto updateTipBoard(Long id, BoardTypeVoteCreateReqDto boardDto);
//
//    // 투표 게시글 삭제
//    void deleteVoteBoard(Long id);

    // 소금모아태산 게시판 (챌린지 달성 정보 작성 게시판)
//    BoardTypeHofReadResDto createHofBoard(BoardTypeHofCreateReqDto requestDto);


}
