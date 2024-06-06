package com.example.savingsalt.global;

import com.example.savingsalt.badge.exception.BadgeException.BadgeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.ChallengeNotFoundException;
import com.example.savingsalt.challenge.exception.ChallengeException.InvalidChallengeGoalAndCountException;
import com.example.savingsalt.challenge.exception.ChallengeException.MemberChallengeFailureException;
import com.example.savingsalt.community.board.exception.BoardException;
import com.example.savingsalt.community.board.exception.CommentException;
import com.example.savingsalt.community.poll.exception.PollException;
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

    @ExceptionHandler(InvalidChallengeGoalAndCountException.class)
    public ResponseEntity<String> handleInvalidChallengeGoalAndCountException(
        InvalidChallengeGoalAndCountException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(MemberChallengeFailureException.class)
    public ResponseEntity<String> handleMemberChallengeFailureException(MemberChallengeFailureException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    // 게시판 관련 예외처리
    @ExceptionHandler(BoardException.UnauthorizedCreateException.class)
    public ResponseEntity<String> handleUnauthorizedCreateException(
        BoardException.UnauthorizedCreateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(BoardException.UnauthorizedUpdateException.class)
    public ResponseEntity<String> handleUnauthorizedUpdateException(
        BoardException.UnauthorizedUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(BoardException.UnauthorizedDeleteException.class)
    public ResponseEntity<String> handleUnauthorizedDeleteException(
        BoardException.UnauthorizedDeleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BoardException.EmptyBoardException.class)
    public ResponseEntity<String> handleEmptyBoardException(
        BoardException.EmptyBoardException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BoardException.InternalServerErrorException.class)
    public ResponseEntity<String> handleInternalServerErrorException(
        BoardException.InternalServerErrorException ex) {
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
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CommentException.ValidateAuthorForDelete.class)
    public ResponseEntity<String> handleValidateAuthorForDelete(
        CommentException.ValidateAuthorForDelete ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 투표 관련 예외 처리
    @ExceptionHandler(PollException.PollNotFoundException.class)
    public ResponseEntity<String> handlePollNotFoundException(PollException.PollNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PollException.ChoiceNotFoundException.class)
    public ResponseEntity<String> handleChoiceNotFoundException(PollException.ChoiceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PollException.UnauthorizedPollAccessException.class)
    public ResponseEntity<String> handleUnauthorizedPollAccessException(PollException.UnauthorizedPollAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PollException.PollCreationException.class)
    public ResponseEntity<String> handlePollCreationException(PollException.PollCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PollException.PollParticipationException.class)
    public ResponseEntity<String> handlePollParticipationException(PollException.PollParticipationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
