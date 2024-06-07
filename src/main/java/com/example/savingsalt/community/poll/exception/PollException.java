package com.example.savingsalt.community.poll.exception;

public class PollException extends RuntimeException{
    public PollException(String message) {
        super(message);
    }

    public static class PollNotFoundException extends PollException {
        public PollNotFoundException(String message) {
            super("해당 투표를 찾을 수 없습니다.");
        }
    }

    public static class ChoiceNotFoundException extends PollException {
        public ChoiceNotFoundException(String message) {
            super("선택지를 찾을 수 없습니다.");
        }
    }

    public static class UnauthorizedPollAccessException extends PollException {
        public UnauthorizedPollAccessException(String message) {
            super("해당 투표에 접근할 권한이 없습니다.");
        }
    }

    public static class PollCreationException extends PollException {
        public PollCreationException(String message) {
            super("투표 생성에 실패하였습니다.");
        }
    }

    public static class PollParticipationException extends PollException {
        public PollParticipationException(String message) {
            super("투표 참여에 실패하였습니다.");
        }
    }
}
