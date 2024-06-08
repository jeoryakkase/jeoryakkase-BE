package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.exception.BoardException.BoardNotFoundException;
import com.example.savingsalt.community.board.exception.BoardException.BoardServiceException;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.comment.domain.dto.CommentResDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentResDto;
import com.example.savingsalt.community.comment.domain.entity.CommentEntity;
import com.example.savingsalt.community.comment.domain.entity.ReplyCommentEntity;
import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.community.comment.repository.ReplyCommentRepository;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResDto;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.service.PollService;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final PollService pollService;

    private final PollRepository pollRepository;

    private final CommentRepository commentRepository;

    private final ReplyCommentRepository replyCommentRepository;


    // 절약팁 게시글 작성
    @Transactional
    @Override
    public BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto,
        MemberEntity member) {
        BoardEntity boardEntity = requestDto.toEntity(member);
        try {
            BoardEntity savedBoardEntity = boardRepository.save(boardEntity);
            return toTipReadDto(savedBoardEntity);
        } catch (Exception e) {
            throw new BoardServiceException("팁 게시글을 작성하는 중 오류가 발생했습니다.", e);
        }
    }

    // 절약팁 게시글 목록 조회
    @Transactional
    @Override
    public Page<BoardTypeTipReadResDto> findAllTipBoard(int page, int size) {
        BoardCategory category = BoardCategory.TIPS;
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
                category, pageable);

            return boards.map(this::toTipReadDto);
        } catch (Exception e) {
            throw new BoardServiceException("팁 게시글 목록을 조회하는 중 오류가 발생했습니다.", e);
        }

    }

    // 절약팁 게시글 조회
    @Transactional
    @Override
    public BoardTypeTipReadResDto findTipBoardById(Long id) {
        BoardCategory category = BoardCategory.TIPS;
        BoardEntity boardEntity = boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new BoardNotFoundException());

        boardEntity.incrementView();
        try {
            boardRepository.save(boardEntity);

            List<CommentEntity> comments = commentRepository.findAllByBoardEntityIdOrderByCreatedAtAsc(
                boardEntity.getId());
            List<CommentResDto> commentDtos = comments.stream()
                .map(this::toCommentResDto)
                .collect(Collectors.toList());

            return toTipReadDto(boardEntity, commentDtos);
        } catch (Exception e) {
            throw new BoardServiceException("팁 게시글을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 절약팁 게시글 수정
    @Transactional
    @Override
    public BoardTypeTipReadResDto updateTipBoard(Long id, BoardTypeTipCreateReqDto requestDto,
        MemberEntity member) {
        BoardEntity board = findBoard(id, requestDto.getCategory());

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostUpdateException();
        }

        try {
            board.updateTipBoard(requestDto);
            BoardEntity updatedBoard = boardRepository.save(board);
            return toTipReadDto(updatedBoard);
        } catch (Exception e) {
            throw new BoardServiceException("팁 게시글을 수정하는 중 오류가 발생했습니다.", e);
        }
    }

    // 절약팁 게시글 삭제
    @Transactional
    @Override
    public void deleteTipBoard(Long id, MemberEntity member) {
        BoardCategory category = BoardCategory.TIPS;
        BoardEntity board = findBoard(id, category);

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostDeleteException();
        }

        try {
            boardRepository.delete(board);
        } catch (Exception e) {
            throw new BoardServiceException("팁 게시글을 삭제하는 중 오류가 발생했습니다.", e);
        }
    }

    // 허불허 게시판 (투표게시판)

    // 투표 게시글 작성
    @Transactional
    @Override
    public BoardTypeVoteReadResDto createVoteBoard(BoardTypeVoteCreateReqDto requestDto,
        MemberEntity member) {
        try {
            BoardEntity board = requestDto.toEntity(member);
            boardRepository.save(board);

            PollEntity poll = requestDto.toPollEntity(board);
            pollRepository.save(poll);

            return toVoteReadDto(board);
        } catch (Exception e) {
            throw new BoardServiceException("투표 게시글을 작성하는 중 오류가 발생했습니다.", e);
        }
    }

    // 투표 게시글 목록 조회
    @Transactional
    @Override
    public Page<BoardTypeVoteReadResDto> findAllVoteBoard(int page, int size) {
        BoardCategory category = BoardCategory.VOTE;
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
                category, pageable);
            return boards.map(this::toVoteReadDto);
        } catch (Exception e) {
            throw new BoardServiceException("투표 게시글 목록을 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 투표 게시글 조회
    @Transactional
    @Override
    public BoardTypeVoteReadResDto findVoteBoardById(Long id) {
        BoardCategory category = BoardCategory.VOTE;

        BoardEntity boardEntity = boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new BoardNotFoundException());

        boardEntity.incrementView();

        try {
            boardRepository.save(boardEntity);

            List<CommentEntity> comments = commentRepository.findAllByBoardEntityIdOrderByCreatedAtAsc(
                boardEntity.getId());
            List<CommentResDto> commentDtos = comments.stream()
                .map(this::toCommentResDto)
                .collect(Collectors.toList());

            return toVoteReadDto(boardEntity, commentDtos);
        } catch (Exception e) {
            throw new BoardServiceException("투표 게시글을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 투표 게시글 수정
    @Transactional
    @Override
    public BoardTypeVoteReadResDto updateVoteBoard(Long id, BoardTypeVoteCreateReqDto requestDto,
        MemberEntity member) {

        BoardEntity board = findBoard(id, requestDto.getCategory());

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostUpdateException();
        }

        try {
            board.updateVoteBoard(requestDto);

            BoardEntity updatedBoard = boardRepository.save(board);

            return toVoteReadDto(updatedBoard);
        } catch (Exception e) {
            throw new BoardServiceException("투표 게시글을 수정하는 중 오류가 발생했습니다.", e);
        }
    }

    // 투표 게시글 삭제
    @Transactional
    @Override
    public void deleteVoteBoard(Long id, MemberEntity member) {

        BoardCategory category = BoardCategory.VOTE;
        BoardEntity board = findBoard(id, category);

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostDeleteException();
        }
        try {
            boardRepository.delete(board);
        } catch (Exception e) {
            throw new BoardServiceException("투표 게시글을 삭제하는 중 오류가 발생했습니다.", e);
        }
    }


    // BoardEntity를 BoardTypeTipReadResDto로 변환
    private BoardTypeTipReadResDto toTipReadDto(BoardEntity boardEntity) {
        return BoardTypeTipReadResDto.builder()
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .totalLike(boardEntity.getTotalLike())
            .view(boardEntity.getView())
            .build();
    }

    private BoardTypeTipReadResDto toTipReadDto(BoardEntity boardEntity,
        List<CommentResDto> comments) {
        return BoardTypeTipReadResDto.builder()
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .comments(comments)
            .totalLike(boardEntity.getTotalLike())
            .view(boardEntity.getView())
            .build();
    }

    private BoardTypeVoteReadResDto toVoteReadDto(BoardEntity boardEntity) {
        PollResDto pollResDto = pollService.findPollByBoardId(boardEntity.getId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .view(boardEntity.getView())
            .pollResDto(pollResDto)
            .build();
    }

    private BoardTypeVoteReadResDto toVoteReadDto(BoardEntity boardEntity,
        List<CommentResDto> comments) {
        PollResDto pollResDto = pollService.findPollByBoardId(boardEntity.getId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .comments(comments)
            .view(boardEntity.getView())
            .pollResDto(pollResDto)
            .build();
    }

    private CommentResDto toCommentResDto(CommentEntity comment) {
        List<ReplyCommentEntity> replies = replyCommentRepository.findAllByParentCommentIdOrderByCreatedAtAsc(
            comment.getId());
        List<ReplyCommentResDto> replyDtos = replies.stream()
            .map(this::toReplyCommentDto)
            .collect(Collectors.toList());

        return CommentResDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .nickname(comment.getNickname())
            .replyComments(replyDtos)
            .build();
    }

    private ReplyCommentResDto toReplyCommentDto(ReplyCommentEntity replyComment) {
        return ReplyCommentResDto.builder()
            .id(replyComment.getId())
            .content(replyComment.getContent())
            .nickname(replyComment.getMemberEntity().getNickname())
            .build();
    }

    // 카테고리별 게시글 찾기
    private BoardEntity findBoard(Long id, BoardCategory category) {
        return boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new BoardNotFoundException());
    }
}

