package com.example.savingsalt.community.like.exception;

public class LikeException extends RuntimeException{
    public LikeException(String message) {
        super(message);
    }

    public static class MemberNotFoundException extends LikeException {
        public MemberNotFoundException() {
            super("회원을 찾을 수 없습니다.");
        }
    }

    public static class BoardNotFoundException extends LikeException {
        public BoardNotFoundException() {
            super("게시물을 찾을 수 없습니다.");
        }
    }
}
