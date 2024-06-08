package com.example.savingsalt.community.bookmark.exception;

public class BookmarkException extends RuntimeException{
    public BookmarkException(String message) {
        super(message);
    }

    public static class MemberNotFoundException extends BookmarkException {
        public MemberNotFoundException() {
            super("회원을 찾을 수 없습니다.");
        }
    }

    public static class BoardNotFoundException extends BookmarkException {
        public BoardNotFoundException() {
            super("게시물을 찾을 수 없습니다.");
        }
    }
}
