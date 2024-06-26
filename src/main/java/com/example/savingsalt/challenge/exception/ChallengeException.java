package com.example.savingsalt.challenge.exception;

import java.io.IOException;

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

    public static class InvalidChallengeGoalAndCountException extends RuntimeException {

        public InvalidChallengeGoalAndCountException() {
            super("챌린지 목표 금액, 챌린지 목표 횟수 중 하나만 꼭 존재해야 됩니다.");
        }
    }

    public static class MemberChallengeAlreadySucceededException extends RuntimeException {
        public MemberChallengeAlreadySucceededException() {
            super("이미 완료된 챌린지입니다.");
        }
    }

    public static class ResourceCreationException extends IOException {
        public ResourceCreationException() {
            super("이미지 업로드가 실패했습니다.");
        }
    }

    public static class CertificationChallengeNotFoundException extends RuntimeException {
        public CertificationChallengeNotFoundException() {
            super("챌린지 인증 테이블이 존재 하지 않습니다.");
        }
    }
}
