package com.example.savingsalt.member.exception;

public class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public static class EmailAlreadyExistsException extends MemberException {

        public EmailAlreadyExistsException() {
            super("Member with this email already exists.");
        }

        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class NicknameAlreadyExistsException extends MemberException {

        public NicknameAlreadyExistsException() {
            super("Member with this nickname already exists.");
        }
    }

    public static class MemberNotFoundException extends MemberException {

        public MemberNotFoundException(String field, Object value) {
            super(String.format("Member with %s '%s' not found.", field, value));
        }
    }

    public static class RefreshTokenNotFoundException extends MemberException {

        public RefreshTokenNotFoundException() {
            super("Unexpected token.");
        }
    }

    public static class InvalidTokenException extends MemberException {

        public InvalidTokenException() {
            super("Invalid token.");
        }
    }

    public static class InvalidPasswordException extends MemberException {

        public InvalidPasswordException() {
            super("Password doesn't match.");
        }
    }
}
