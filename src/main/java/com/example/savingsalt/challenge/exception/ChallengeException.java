package com.example.savingsalt.challenge.exception;

public class ChallengeException {

    public static class MemberChallengeNotFoundException extends RuntimeException {

        public MemberChallengeNotFoundException() {
            super("회원 챌린지를 찾을 수 없습니다.");
        }
    }

    public static class ChallengeNotFoundException extends RuntimeException {

        public ChallengeNotFoundException() {
            super("해당 챌린지를 찾을 수 없습니다.");
        }
    }

    public static class MemberChallengeFailureException extends RuntimeException {

        public MemberChallengeFailureException() {
            super("회원 챌린지 조건에 만족하지 않아 챌린지 인증이 실패했습니다.");
        }
    }

    public static class InvalidChallengeGoalAndCountException extends RuntimeException {

        public InvalidChallengeGoalAndCountException() {
            super("챌린지 목표 금액, 챌린지 목표 횟수 중 하나만 꼭 존재해야 됩니다.");
        }
    }
}
