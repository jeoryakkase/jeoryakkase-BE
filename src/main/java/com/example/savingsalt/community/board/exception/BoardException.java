package com.example.savingsalt.community.board.exception;

public class BoardException extends RuntimeException {

    public BoardException(String message) {
        super(message);
    }

    public BoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class UnauthorizedPostCreateException extends BoardException {

        public UnauthorizedPostCreateException() {
            super("게시글 작성 권한이 없습니다.");
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

    public static class UnauthorizedPostUpdateException extends BoardException {

        public UnauthorizedPostUpdateException() {
            super("게시글 수정 권한이 없습니다.");
        }
    }

    public static class UnauthorizedPostDeleteException extends BoardException {

        public UnauthorizedPostDeleteException() {
            super("게시글 삭제 권한이 없습니다.");
        }
    }

    public static class ImageNotFoundException extends BoardException{

        public ImageNotFoundException() {
            super("이미지를 찾을 수 없습니다.");
        }

    }

    public static class BoardServiceException extends BoardException {

        public BoardServiceException(String message) {
            super(message);
        }

        public BoardServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
