package com.example.savingsalt.badge.exception;

public class BadgeException {

    public static class BadgeNotFoundException extends RuntimeException {

        public BadgeNotFoundException() {
            super("해당 뱃지 찾을 수 없습니다.");
        }
    }

}
