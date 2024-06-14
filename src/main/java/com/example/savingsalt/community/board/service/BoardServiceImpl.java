package com.example.savingsalt.community.board.service;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.service.BadgeService;
import com.example.savingsalt.community.board.domain.dto.BoardImageDto;
import com.example.savingsalt.community.board.domain.dto.BoardMainDto;
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
import com.example.savingsalt.community.board.exception.BoardException.ImageNotFoundException;
import com.example.savingsalt.community.board.repository.BoardImageRepository;
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
import com.example.savingsalt.config.s3.S3Service;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.io.IOException;
import java.util.ArrayList;
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

    private final BoardImageRepository boardImageRepository;

    private final BadgeService badgeService;

    private final S3Service s3Service;


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

        Page<BoardEntity> boards = boardRepository.findAllByCategoryOrderByCreatedAtDesc(
            category, pageable);

        return boards.map(this::convertToTipReadResDto);
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
        MemberEntity member, List<String> newImageUrls, List<String> deleteImageUrls) {

        BoardEntity board = findBoard(id, requestDto.getCategory());

        if (!board.getMemberEntity().getId().equals(member.getId())) {
            throw new BoardException.UnauthorizedPostUpdateException();
        }

        // 이미지 삭제 처리
        if (deleteImageUrls != null && !deleteImageUrls.isEmpty()) {
            for (String imageUrl : deleteImageUrls) {
                BoardImageEntity imageEntity = boardImageRepository.findByImageUrl(imageUrl)
                    .orElseThrow(() -> new ImageNotFoundException());
                boardImageRepository.delete(imageEntity);
                try {
                    s3Service.deleteFile(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // 새 이미지 추가 처리
        List<BoardImageEntity> newBoardImages = new ArrayList<>();
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            for (String imageUrl : newImageUrls) {
                BoardImageEntity imageEntity = BoardImageEntity.builder()
                    .boardEntity(board)
                    .imageUrl(imageUrl)
                    .build();
                newBoardImages.add(boardImageRepository.save(imageEntity));
            }
        }
        List<BoardImageEntity> allBoardImages = boardImageRepository.findAllByBoardEntityId(board.getId());
        List<BoardImageDto> imageDtos = toImageDtos(allBoardImages);

        // 게시글 업데이트
        BoardEntity updateBoard = board.toBuilder()
            .title(Optional.ofNullable(requestDto.getTitle()).orElse(board.getTitle()))
            .contents(Optional.ofNullable(requestDto.getContents()).orElse(board.getContents()))
            .build();

        List<CommentEntity> comments = commentRepository.findAllByBoardEntityIdOrderByCreatedAtAsc(
            board.getId());

        List<CommentResDto> commentDtos = comments.stream()
            .map(this::toCommentResDto)
            .collect(Collectors.toList());

        try {
            BoardEntity updatedBoard = boardRepository.save(updateBoard);
            return convertToTipReadResDto(updatedBoard, commentDtos, imageDtos);
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

        pollService.createPollForBoard(savedBoardEntity.getId());

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

    // 메인 게시글 조회

    public BoardMainDto getLatestTipBoard() {
        BoardEntity latestTipBoard = boardRepository.findFirstByCategoryOrderByCreatedAtDesc(
            BoardCategory.TIPS).orElseThrow(() -> new BoardNotFoundException());

        List<BoardImageEntity> images = boardImageService.findAllImageByBoardId(
            latestTipBoard.getId());
        List<BoardImageDto> imageDtos = toImageDtos(images);

        return convertToMainTipDto(latestTipBoard, imageDtos);
    }

    public BoardMainDto getLatestVoteBoard() {
        BoardEntity latestVoteBoard = boardRepository.findFirstByCategoryOrderByCreatedAtDesc(
            BoardCategory.VOTE).orElseThrow(() -> new BoardNotFoundException());

        return convertToMainVoteDto(latestVoteBoard);
    }


    // 메인 조회 Dto
    private BoardMainDto convertToMainTipDto(BoardEntity boardEntity,
        List<BoardImageDto> imageDtos) {

        return BoardMainDto.builder()
            .id(boardEntity.getId())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .boardImageDtos(imageDtos)
            .build();
    }

    private BoardMainDto convertToMainVoteDto(BoardEntity boardEntity) {

        return BoardMainDto.builder()
            .id(boardEntity.getId())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .build();
    }

    // 팁 게시글 작성 반환 dto
    private BoardTypeTipReadResDto convertToTipReadResDto(BoardEntity boardEntity) {

        BadgeDto badgeDto = toBadgeDto(boardEntity.getRepresentativeBadgeId());

        return BoardTypeTipReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .profileImage(boardEntity.getProfileImage())
            .badgeDto(badgeDto)
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .totalLike(boardEntity.getTotalLike())
            .view(boardEntity.getView())
            .boardImageDtos(toImageDtos(boardEntity.getBoardImageEntities()))
            .createdAt(boardEntity.getCreatedAt())
            .modifiedAt(boardEntity.getModifiedAt())
            .build();
    }

    private BoardTypeTipReadResDto convertToTipReadResDto(BoardEntity boardEntity,
        List<CommentResDto> comments, List<BoardImageDto> imageDtos) {

        BadgeDto badgeDto = toBadgeDto(boardEntity.getRepresentativeBadgeId());

        return BoardTypeTipReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .profileImage(boardEntity.getProfileImage())
            .badgeDto(badgeDto)
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

        BadgeDto badgeDto = toBadgeDto(boardEntity.getRepresentativeBadgeId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .profileImage(boardEntity.getProfileImage())
            .badgeDto(badgeDto)
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

        BadgeDto badgeDto = toBadgeDto(boardEntity.getRepresentativeBadgeId());

        return BoardTypeVoteReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMemberEntity().getNickname())
            .profileImage(boardEntity.getProfileImage())
            .badgeDto(badgeDto)
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
            .commentId(comment.getId())
            .content(comment.getContent())
            .nickname(comment.getMemberEntity().getNickname())
            .replyComments(replyDtos)
            .build();
    }

    private ReplyCommentResDto toReplyCommentDto(ReplyCommentEntity replyComment) {

        return ReplyCommentResDto.builder()
            .replyId(replyComment.getId())
            .content(replyComment.getContent())
            .nickname(replyComment.getMemberEntity().getNickname())
            .build();
    }


    private List<BoardImageDto> toImageDtos(List<BoardImageEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
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

    // 프로필 뱃지 가져오기
    private BadgeDto toBadgeDto(Long badgeId) {

        if (badgeId == null) {
            return BadgeDto.builder()
                .name(null)
                .badgeImage(null)
                .badgeDesc(null)
                .badgeType(null)
                .build();
        }

        BadgeEntity badgeEntity = badgeService.findById(badgeId);

        return BadgeDto.builder()
            .name(badgeEntity.getName())
            .badgeImage(badgeEntity.getBadgeImage())
            .badgeDesc(badgeEntity.getBadgeDesc())
            .badgeType(badgeEntity.getBadgeType())
            .build();
    }
}



