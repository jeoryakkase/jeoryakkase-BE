package com.example.savingsalt.global;

import com.example.savingsalt.badge.exception.BadgeException.BadgeNotFoundException;
import com.example.savingsalt.badge.exception.BadgeException.InvalidRepresentativeBadgeException;
import com.example.savingsalt.badge.exception.BadgeException.RepresentativeBadgeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.ChallengeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.ChallengeTypeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.InvalidChallengeGoalAndCountException;
import com.example.savingsalt.challenge.exception.ChallengeException.MemberChallengeAlreadySucceededException;
import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.exception.BoardException.BoardNotFoundException;
import com.example.savingsalt.community.board.exception.BoardException.BoardServiceException;
import com.example.savingsalt.community.bookmark.exception.BookmarkException;
import com.example.savingsalt.community.comment.exception.CommentException;
import com.example.savingsalt.community.like.exception.LikeException;
import com.example.savingsalt.community.poll.exception.PollException;
import com.example.savingsalt.goal.exception.CertificationNotFoundException;
import com.example.savingsalt.goal.exception.MaxProceedingGoalsExceededException;
import com.example.savingsalt.goal.exception.PermissionDeniedException;
import com.example.savingsalt.member.exception.MemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(
        MemberException.EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MemberException.NicknameAlreadyExistsException.class)
    public ResponseEntity<String> handleNicknameAlreadyExistsException(
        MemberException.NicknameAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MemberException.RefreshTokenNotFoundException.class)
    public ResponseEntity<String> handleRefreshTokenNotFoundException(
        MemberException.RefreshTokenNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberException.InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(
        MemberException.InvalidTokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberException.MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(
        MemberException.MemberNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberException.InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(
        MemberException.InvalidPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadgeNotFoundException.class)
    public ResponseEntity<String> handleBadgeNotFoundException(BadgeNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ChallengeNotFoundException.class)
    public ResponseEntity<String> handleChallengeNotFoundException(ChallengeNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ChallengeTypeNotFoundException.class)
    public ResponseEntity<String> handleChallengeTypeNotFoundException(
        ChallengeTypeNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidChallengeGoalAndCountException.class)
    public ResponseEntity<String> handleInvalidChallengeGoalAndCountException(
        InvalidChallengeGoalAndCountException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
            builder.append("\n");
        }

        return new ResponseEntity<>(builder.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepresentativeBadgeNotFoundException.class)
    public ResponseEntity<String> handleRepresentativeBadgeNotFoundException(
        RepresentativeBadgeNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberChallengeAlreadySucceededException.class)
    public ResponseEntity<String> handleMemberChallengeAlreadySucceededException(
        MemberChallengeAlreadySucceededException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRepresentativeBadgeException.class)
    public ResponseEntity<String> handleInvalidRepresentativeBadgeException(
        InvalidRepresentativeBadgeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 게시판 관련 예외처리
    @ExceptionHandler(BoardException.UnauthorizedPostCreateException.class)
    public ResponseEntity<String> handleUnauthorizedPostCreateException(
        BoardException.UnauthorizedPostCreateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BoardException.UnauthorizedPostUpdateException.class)
    public ResponseEntity<String> handleUnauthorizedPostUpdateException(
        BoardException.UnauthorizedPostUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BoardException.UnauthorizedPostDeleteException.class)
    public ResponseEntity<String> handleUnauthorizedPostDeleteException(
        BoardException.UnauthorizedPostDeleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BoardException.EmptyBoardException.class)
    public ResponseEntity<String> handleEmptyBoardException(
        BoardException.EmptyBoardException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<String> handleBoardNotFoundException(BoardNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardServiceException.class)
    public ResponseEntity<String> handleBoardServiceException(BoardServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // 댓글 관련 예외 처리
    @ExceptionHandler(CommentException.CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(
        CommentException.CommentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentException.ValidateAuthorForUpdate.class)
    public ResponseEntity<String> handleValidateAuthorForUpdate(
        CommentException.ValidateAuthorForUpdate ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CommentException.ValidateAuthorForDelete.class)
    public ResponseEntity<String> handleValidateAuthorForDelete(
        CommentException.ValidateAuthorForDelete ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CommentException.CannotUpdateCommentWithReplies.class)
    public ResponseEntity<String> handleCannotUpdateCommentWithReplies(
        CommentException.CannotUpdateCommentWithReplies ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentException.NotFoundParentComment.class)
    public ResponseEntity<String> handleNotFoundParentComment(
        CommentException.NotFoundParentComment ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 투표 관련 예외 처리
    @ExceptionHandler(PollException.PollNotFoundException.class)
    public ResponseEntity<String> handlePollNotFoundException(
        PollException.PollNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PollException.MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(PollException.MemberNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PollException.UserAlreadyVotedException.class)
    public ResponseEntity<String> handleUserAlreadyVotedException(PollException.UserAlreadyVotedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(MaxProceedingGoalsExceededException.class)
    public ResponseEntity<String> handleMaxProceedingGoalsExceeded(
        MaxProceedingGoalsExceededException ex) {
        // 진행중인 목표가 5개를 초과했을 때 처리
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 좋아요 예외 처리
    @ExceptionHandler(LikeException.MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(
        LikeException.MemberNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LikeException.BoardNotFoundException.class)
    public ResponseEntity<String> handleBoardNotFoundException(
        LikeException.BoardNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 북마크 예외 처리
    @ExceptionHandler(BookmarkException.MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(
        BookmarkException.MemberNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookmarkException.BoardNotFoundException.class)
    public ResponseEntity<String> handleBoardNotFoundException(
        BookmarkException.BoardNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<String> handlePermissionDenied(PermissionDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(CertificationNotFoundException.class)
    public ResponseEntity<String> handleCertificationNotFoundException(
        CertificationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
