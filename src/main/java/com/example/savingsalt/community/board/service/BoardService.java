package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.member.domain.MemberEntity;
import org.springframework.data.domain.Page;


public interface BoardService {

    // 절약팁 게시판 (like, bookmark 기능)

    // 절약팁 게시글 작성
    BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto, MemberEntity member);

    // 절약팁 게시글 목록 조회
    Page<BoardTypeTipReadResDto> findAllTipBoard(int page, int size);
    // 절약팁 게시글 조회
    BoardTypeTipReadResDto findTipBoardById(Long id);
    // 절약팁 게시글 수정
    BoardTypeTipReadResDto updateTipBoard(Long id, BoardTypeTipCreateReqDto boardDto, MemberEntity member);
    // 절약팁 게시글 삭제
    void deleteTipBoard(Long id, MemberEntity member);

    // 허불허 게시판 (투표게시판)

    // 투표 게시글 작성
    BoardTypeVoteReadResDto createVoteBoard(BoardTypeVoteCreateReqDto requestDto, MemberEntity memberEntity);

    // 투표 게시글 목록 조회
    Page<BoardTypeVoteReadResDto> findAllVoteBoard(int page, int size);

    // 투표 게시글 조회
    BoardTypeVoteReadResDto findVoteBoardById(Long id);

    // 투표 게시글 수정
    BoardTypeVoteReadResDto updateVoteBoard(Long id, BoardTypeVoteCreateReqDto boardDto, MemberEntity member);

    // 투표 게시글 삭제
    void deleteVoteBoard(Long id, MemberEntity member);

}
