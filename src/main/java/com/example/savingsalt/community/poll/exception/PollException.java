package com.example.savingsalt.community.poll.exception;

public class PollException extends RuntimeException{
    public PollException(String message) {
        super(message);
    }

    public static class PollNotFoundException extends PollException {
        public PollNotFoundException() {
            super("해당 투표를 찾을 수 없습니다.");
        }
    }

    public static class MemberNotFoundException extends PollException {
        public MemberNotFoundException() {
            super("해당 회원을 찾을 수 없습니다.");
        }
    }

    public static class UserAlreadyVotedException extends PollException {
        public UserAlreadyVotedException() {
            super("사용자가 이미 투표했습니다.");
        }
    }
}
