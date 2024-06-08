package com.example.savingsalt.badge.exception;

public class BadgeException {

    public static class BadgeNotFoundException extends RuntimeException {

        public BadgeNotFoundException() {
            super("해당 뱃지를 찾을 수 없습니다.");
        }
    }

    public static class RepresentativeBadgeNotFoundException extends RuntimeException {

        public RepresentativeBadgeNotFoundException() {
            super("대표 뱃지를 찾을 수 없습니다.");
        }
    }

    public static class InvalidRepresentativeBadgeException extends RuntimeException {

        public InvalidRepresentativeBadgeException() {
            super("해당 뱃지를 대표 뱃지로 등록 할 수 없습니다.");
        }
    }

}
