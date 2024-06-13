package com.example.savingsalt.challenge.exception;

public class ChallengeException {

    public static class ChallengeNotFoundException extends RuntimeException {

        public ChallengeNotFoundException() {
            super(ChallengeExceptionType.NOT_FOUND_CHALLENGE.getExceptionMessage());
        }
    }

    public static class InvalidChallengeGoalAndCountException extends RuntimeException {

        public InvalidChallengeGoalAndCountException() {
            super(ChallengeExceptionType.INVALID_CHALLENGE_TYPE.getExceptionMessage());
        }
    }

    public static class ChallengeTypeNotFoundException extends RuntimeException {

        public ChallengeTypeNotFoundException(String keyword) {
            super(keyword + ChallengeExceptionType.NOT_FOUND_CHALLENGE_TYPE.getExceptionMessage());
        }
    }

    public static class MemberChallengeNotFoundException extends RuntimeException {

        public MemberChallengeNotFoundException() {
            super(ChallengeExceptionType.NOT_FOUND_MEMBER_CHALLENGE.getExceptionMessage());
        }
    }

    public static class MemberChallengeAlreadySucceededException extends RuntimeException {

        public MemberChallengeAlreadySucceededException() {
            super(ChallengeExceptionType.ALREADY_SUCCEEDED.getExceptionMessage());
        }
    }

    public static class CertificationChallengeNotFoundException extends RuntimeException {

        public CertificationChallengeNotFoundException() {
            super(ChallengeExceptionType.NOT_FOUND_CERTIFICATION_CHALLENGE.getExceptionMessage());
        }
    }

    public static class InvalidChallengeTermException extends RuntimeException {

        public InvalidChallengeTermException() {
            super(ChallengeExceptionType.INVALID_CHALLENGE_TERM.getExceptionMessage());
        }
    }

    public static class AlreadyInProgressMemberChallengeException extends RuntimeException {
        public AlreadyInProgressMemberChallengeException() {
            super(ChallengeExceptionType.ALREADY_IN_PROGRESS_CHALLENGE.getExceptionMessage());
        }
    }
}
