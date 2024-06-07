package com.example.savingsalt.community.board.exception;

public class CommentException extends RuntimeException {

    public CommentException(String message) {
        super(message);
    }

    public static class CommentNotFoundException extends CommentException {
        public CommentNotFoundException() {
            super("해당 댓글은 존재하지 않습니다.");
        }
    }

    public static class ValidateAuthorForUpdate extends CommentException {
        public ValidateAuthorForUpdate() {
            super("작성자만 수정 가능합니다.");
        }
    }

    public static class ValidateAuthorForDelete extends CommentException {
        public ValidateAuthorForDelete() {
            super("작성자만 삭제 가능합니다.");
        }
    }



}
