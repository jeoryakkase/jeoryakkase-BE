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
}
