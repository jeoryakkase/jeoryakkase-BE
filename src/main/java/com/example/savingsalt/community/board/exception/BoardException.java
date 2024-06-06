package com.example.savingsalt.community.board.exception;

public class BoardException extends RuntimeException {

    public BoardException(String message) {
        super(message);
    }

    public static class UnauthorizedCreateException extends BoardException {
        public UnauthorizedCreateException() {
            super("게시글 작성 권한이 없습니다.");
        }
    }

    public static class BadRequestException extends BoardException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class EmptyBoardException extends BoardException {

        public EmptyBoardException() {
            super("게시글이 존재하지 않습니다.");
        }

    }

    public static class BoardNotFoundException extends BoardException {
        public BoardNotFoundException() {
            super("해당 게시글을 찾을 수 없습니다.");
        }
    }

    public static class UnauthorizedUpdateException extends BoardException {
        public UnauthorizedUpdateException() {
            super("게시글 수정 권한이 없습니다.");
        }
    }

    public static class UnauthorizedDeleteException extends BoardException {
        public UnauthorizedDeleteException() {
            super("게시글 삭제 권한이 없습니다.");
        }
    }

    public static class InternalServerErrorException extends BoardException {
        public InternalServerErrorException() {
            super("게시글 작성 중 오류가 발생했습니다.");
        }
    }

}
