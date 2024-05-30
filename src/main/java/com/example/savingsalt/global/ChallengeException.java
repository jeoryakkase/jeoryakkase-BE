package com.example.savingsalt.global;

public class ChallengeException {
    public static class MemberChallengeNotFoundException extends RuntimeException {
        public MemberChallengeNotFoundException() {
            super("회원 챌린지를 찾을 수 없습니다.");
        }
    }
}
