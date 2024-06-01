package com.example.savingsalt.community.board.service;

import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.community.board.domain.BoardCategory;
import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.board.domain.BoardTypeHofCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeHofReadResDto;
import com.example.savingsalt.community.board.domain.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.poll.domain.Poll;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.service.PollService;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final PollService pollService;

    private final PollRepository pollRepository;

    private final MemberChallengeRepository memberChallengeRepository;

    // 절약팁 게시글 작성
    @Override
    public BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto,
        MemberEntity member) {

        BoardEntity boardEntity = requestDto.toEntity(member, BoardCategory.TIPS);

        BoardEntity savedBoardEntity;

        try {
            savedBoardEntity = boardRepository.save(boardEntity);
        } catch (Exception e) {
            throw new RuntimeException("게시글 저장 중 오류가 발생했습니다.", e);
        }

        return convertToBoardTypeTipReadResDto(savedBoardEntity);
    }

    // 절약팁 게시글 목록 조회
    @Override
    public List<BoardTypeTipReadResDto> findAllTipBoard() {
        BoardCategory category = BoardCategory.TIPS;
        List<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
            category);
        return boards.stream()
            .map(this::convertToBoardTypeTipReadResDto)
            .collect(Collectors.toList());
    }

    // 절약팁 게시글 조회
    @Override
    public BoardTypeTipReadResDto findTipBoardById(Long id) {
        BoardCategory category = BoardCategory.TIPS;

        BoardEntity boardEntity = boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToBoardTypeTipReadResDto(boardEntity);
    }

    // 절약팁 게시글 수정
    @Override
    public BoardTypeTipReadResDto updateTipBoard(Long id, BoardTypeTipCreateReqDto requestDto,
        MemberEntity member) {
        BoardCategory category = BoardCategory.TIPS;

        BoardEntity board = findBoard(id, category);

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }

        board.updateTipBoard(requestDto);

        BoardEntity updatedBoard = boardRepository.save(board);

        return convertToBoardTypeTipReadResDto(updatedBoard);
    }

    // 절약팁 게시글 삭제
    @Override
    public void deleteTipBoard(Long id, MemberEntity member) {

        BoardCategory category = BoardCategory.TIPS;
        BoardEntity board= findBoard(id, category);

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        boardRepository.delete(board);
    }

    // 허불허 게시판 (투표게시판)

    // 투표 게시글 작성
    @Override
    public BoardTypeVoteReadResDto createVoteBoard(BoardTypeVoteCreateReqDto requestDto,
        MemberEntity member) {
        BoardCategory category = BoardCategory.VOTE;
        BoardEntity board =requestDto.toEntity(member, category);
        boardRepository.save(board);

        Poll poll = requestDto.toPollEntity(board);
        pollRepository.save(poll);

        return convertToBoardTypeVoteReadResDto(board);
    }

    // 투표 게시글 목록 조회
    @Override
    public List<BoardTypeVoteReadResDto> findAllVoteBoard() {
        BoardCategory category = BoardCategory.VOTE;
        List<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
            category);
        return boards.stream()
            .map(this::convertToBoardTypeVoteReadResDto)
            .collect(Collectors.toList());
    }

    // 투표 게시글 조회
    @Override
    public BoardTypeVoteReadResDto findVoteBoardById(Long id) {
        BoardCategory category = BoardCategory.VOTE;

        BoardEntity boardEntity = boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToBoardTypeVoteReadResDto(boardEntity);
    }

    // 투표 게시글 수정
    @Override
    public BoardTypeVoteReadResDto updateVoteBoard(Long id, BoardTypeVoteCreateReqDto requestDto,
        MemberEntity member) {
        BoardCategory category = BoardCategory.VOTE;

        BoardEntity board = findBoard(id, category);

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }

        board.updateVoteBoard(requestDto);

        BoardEntity updatedBoard = boardRepository.save(board);

        return convertToBoardTypeVoteReadResDto(updatedBoard);
    }

    // 투표 게시글 삭제
    @Override
    public void deleteVoteBoard(Long id) {

        BoardCategory category = BoardCategory.VOTE;
        BoardEntity board = findBoard(id, category);

        boardRepository.delete(board);
    }

    // 소금모아태산 게시판 (챌린지 달성 정보 작성 게시판)

    // 달성 정보 게시글 작성
    @Override
    public BoardTypeHofReadResDto createHofBoard(BoardTypeHofCreateReqDto requestDto) {
        return null;
    }

    // 달성 정보 게시글 목록 조회
    @Override
    public List<BoardTypeHofReadResDto> findAllHofBoard() {
        BoardCategory category = BoardCategory.HALL_OF_FAME;
        List<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
            category);
        return boards.stream()
            .map(this::convertToBoardTypeHofReadResDto)
            .collect(Collectors.toList());
    }


    // 달성 정보 게시글 삭제
    @Override
    public void deleteHofBoard(Long id) {
    }


    // BoardEntity를 BoardTypeTipReadResDto로 변환
    private BoardTypeTipReadResDto convertToBoardTypeTipReadResDto(BoardEntity boardEntity) {
        return BoardTypeTipReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .totalLike(boardEntity.getTotalLike())
            .boardHits(boardEntity.getBoardHits())
            .build();
    }

    private BoardTypeVoteReadResDto convertToBoardTypeVoteReadResDto(BoardEntity boardEntity) {
        PollResDto pollResDto = pollService.findPollByBoardId(boardEntity.getId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .boardHits(boardEntity.getBoardHits())
            .pollResDto(pollResDto)
            .build();
    }

    private BoardTypeHofReadResDto convertToBoardTypeHofReadResDto(BoardEntity boardEntity) {

        return BoardTypeHofReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .totalLike(boardEntity.getTotalLike())
//            .achievements()
            .build();

    }

    // 카테고리별 게시글 찾기
    private BoardEntity findBoard(Long id, BoardCategory category) {
        return boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new IllegalArgumentException("선택한 게시글은 존재하지 않습니다."));
    }
}

