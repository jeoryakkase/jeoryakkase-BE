package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.dto.BoardImageDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteUpdateReqDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.domain.entity.BoardImageEntity;
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
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.exception.PollException.PollNotFoundException;
import com.example.savingsalt.community.poll.repository.PollRepository;
import com.example.savingsalt.community.poll.service.PollServiceImpl;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final PollServiceImpl pollService;

    private final CommentRepository commentRepository;

    private final ReplyCommentRepository replyCommentRepository;

    private final PollRepository pollRepository;

    private final BoardImageService boardImageService;


    // 절약팁 게시글 작성
    @Transactional
    @Override
    public BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto,
        MemberEntity member, List<String> imageUrls) {

        BoardEntity boardEntity = requestDto.toEntity(member);

        BoardEntity savedBoardEntity = boardRepository.save(boardEntity);
        BoardTypeTipReadResDto boardTypeTipReadResDto = convertToTipReadResDto(savedBoardEntity);

        boardTypeTipReadResDto = boardTypeTipReadResDto.toBuilder()
            .boardImageDtos(boardImageService.createBoardImage(imageUrls, savedBoardEntity.getId()))
            .build();

        return boardTypeTipReadResDto;

    }

    // 절약팁 게시글 목록 조회
    @Transactional
    @Override
    public Page<BoardTypeTipReadResDto> findAllTipBoard(int page, int size) {
        BoardCategory category = BoardCategory.TIPS;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            Page<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
                category, pageable);

            return boards.map(this::convertToTipReadResDto);
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

        List<BoardImageEntity> images = boardImageService.findAllImageByBoardId(
            boardEntity.getId());
        List<BoardImageDto> imageDtos = toImageDtos(images);

        List<CommentEntity> comments = commentRepository.findAllByBoardEntityIdOrderByCreatedAtAsc(
            boardEntity.getId());

        if (comments == null) {
            return convertToTipReadResDto(boardEntity);
        }

        List<CommentResDto> commentDtos = comments.stream()
            .map(this::toCommentResDto)
            .collect(Collectors.toList());

        boardEntity.incrementView();

        return convertToTipReadResDto(boardEntity, commentDtos, imageDtos);

    }

    // 절약팁 게시글 수정
    @Transactional
    @Override
    public BoardTypeTipReadResDto updateTipBoard(Long id, BoardTypeTipCreateReqDto requestDto,
        MemberEntity member, List<String> newImageUrls) {
        BoardEntity board = findBoard(id, requestDto.getCategory());

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostUpdateException();
        }

        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            boardImageService.deleteBoardImage(newImageUrls);
            boardImageService.createBoardImage(newImageUrls, board.getId());
        }

        BoardEntity updateBoard = board.toBuilder()
            .title(Optional.ofNullable(requestDto.getTitle()).orElse(board.getTitle()))
            .contents(Optional.ofNullable(requestDto.getContents()).orElse(board.getContents()))
            .build();

        try {
            BoardEntity updatedBoard = boardRepository.save(updateBoard);
            return convertToTipReadResDto(updatedBoard);
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
            boardImageService.deleteAllByBoardEntity(board);
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
        MemberEntity member, List<String> imageUrls) {

        BoardEntity board = requestDto.toEntity(member);

        BoardEntity savedBoardEntity = boardRepository.save(board);
        BoardTypeVoteReadResDto boardTypeVoteReadResDto = convertToVoteReadResDto(savedBoardEntity);

        boardTypeVoteReadResDto = boardTypeVoteReadResDto.toBuilder()
            .boardImageDtos(boardImageService.createBoardImage(imageUrls, savedBoardEntity.getId()))
            .build();

        return boardTypeVoteReadResDto;

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
            return boards.map(this::convertToVoteReadResDto);
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

        List<BoardImageEntity> images = boardImageService.findAllImageByBoardId(
            boardEntity.getId());
        List<BoardImageDto> imageDtos = toImageDtos(images);

        PollEntity pollEntity = pollRepository.findByBoardEntityId(id);
        if (pollEntity == null) {
            throw new PollNotFoundException();

        }
        pollService.getPollResults(pollEntity.getId());

        List<CommentEntity> comments = commentRepository.findAllByBoardEntityIdOrderByCreatedAtAsc(
            boardEntity.getId());

        if (comments == null) {
            return convertToVoteReadResDto(boardEntity);
        }

        List<CommentResDto> commentDtos = comments.stream()
            .map(this::toCommentResDto)
            .collect(Collectors.toList());

        boardEntity.incrementView();

        return convertToVoteReadResDto(boardEntity, commentDtos, imageDtos);

    }

    // 투표 게시글 수정
    @Transactional
    @Override
    public BoardTypeVoteReadResDto updateVoteBoard(Long id, BoardTypeVoteUpdateReqDto requestDto,
        MemberEntity member, List<String> newImageUrls) {

        BoardEntity board = findBoard(id, requestDto.getCategory());

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostUpdateException();
        }

        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            boardImageService.deleteBoardImage(newImageUrls);
            boardImageService.createBoardImage(newImageUrls, board.getId());
        }

        BoardEntity updateBoard = board.toBuilder()
            .title(Optional.ofNullable(requestDto.getTitle()).orElse(board.getTitle()))
            .contents(Optional.ofNullable(requestDto.getContents()).orElse(board.getContents()))
            .build();

        try {
            BoardEntity updatedBoard = boardRepository.save(updateBoard);
            return convertToVoteReadResDto(updatedBoard);
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
            boardImageService.deleteAllByBoardEntity(board);
            boardRepository.delete(board);
        } catch (Exception e) {
            throw new BoardServiceException("투표 게시글을 삭제하는 중 오류가 발생했습니다.", e);
        }
    }

    // BoardEntity를 BoardTypeTipReadResDto로 변환
    private BoardTypeTipReadResDto convertToTipReadResDto(BoardEntity boardEntity) {
        return BoardTypeTipReadResDto.builder()
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .totalLike(boardEntity.getTotalLike())
            .view(boardEntity.getView())
//            .boardImageDtos(boardEntity)
            .createdAt(boardEntity.getCreatedAt())
            .modifiedAt(boardEntity.getModifiedAt())
            .build();
    }

    private BoardTypeTipReadResDto convertToTipReadResDto(BoardEntity boardEntity,
        List<CommentResDto> comments, List<BoardImageDto> imageDtos) {
        return BoardTypeTipReadResDto.builder()
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .comments(comments)
            .totalLike(boardEntity.getTotalLike())
            .view(boardEntity.getView())
            .boardImageDtos(imageDtos)
            .createdAt(boardEntity.getCreatedAt())
            .modifiedAt(boardEntity.getModifiedAt())
            .build();
    }

    private BoardTypeVoteReadResDto convertToVoteReadResDto(BoardEntity boardEntity) {

        PollEntity pollbyBoardEntityId = pollRepository.findByBoardEntityId(
            boardEntity.getId());
        PollResultDto pollResults = pollService.getPollResults(pollbyBoardEntityId.getId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .view(boardEntity.getView())
            .totalLike(boardEntity.getTotalLike())
            .createdAt(boardEntity.getCreatedAt())
            .modifiedAt(boardEntity.getModifiedAt())
            .pollResultDto(pollResults)
            .build();
    }

    private BoardTypeVoteReadResDto convertToVoteReadResDto(BoardEntity boardEntity,
        List<CommentResDto> comments, List<BoardImageDto> imageDtos) {

        PollEntity pollbyBoardEntityId = pollRepository.findByBoardEntityId(
            boardEntity.getId());
        PollResultDto pollResults = pollService.getPollResults(pollbyBoardEntityId.getId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .comments(comments)
            .totalLike(boardEntity.getTotalLike())
            .view(boardEntity.getView())
            .boardImageDtos(imageDtos)
            .createdAt(boardEntity.getCreatedAt())
            .modifiedAt(boardEntity.getModifiedAt())
            .pollResultDto(pollResults)
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
            .nickname(comment.getMemberEntity().getNickname())
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

    private List<BoardImageDto> toImageDtos(List<BoardImageEntity> entities) {
        return entities.stream()
            .map(this::toImageDto)
            .collect(Collectors.toList());
    }

    private BoardImageDto toImageDto(BoardImageEntity boardImageEntity) {
        return BoardImageDto.builder()
            .imageUrl(boardImageEntity.getImageUrl())
            .build();
    }


    // 카테고리별 게시글 찾기
    private BoardEntity findBoard(Long id, BoardCategory category) {
        return boardRepository.findByIdAndCategory(id, category)
            .orElseThrow(() -> new BoardNotFoundException());
    }
}

