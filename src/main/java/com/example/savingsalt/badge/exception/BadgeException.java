package com.example.savingsalt.badge.exception;

public class BadgeException {

    public static class BadgeNotFoundException extends RuntimeException {

        public BadgeNotFoundException() {
            super(BadgeExceptionType.BADGE_NOT_FOUND.getExceptionMessage());
        }
    }

    public static class RepresentativeBadgeNotFoundException extends RuntimeException {

        public RepresentativeBadgeNotFoundException() {
            super(BadgeExceptionType.REPRESENTATIVE_BADGE_NOT_FOUND.getExceptionMessage());
        }
    }

    public static class InvalidRepresentativeBadgeException extends RuntimeException {

        public InvalidRepresentativeBadgeException() {
            super(BadgeExceptionType.INVALID_REPRESENTATIVE_BADGE.getExceptionMessage());
        }
    }

}
