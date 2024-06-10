package com.example.savingsalt.goal.exception;

public class CertificationNotFoundException extends RuntimeException {

    public CertificationNotFoundException() {
        super("해당 인증을 찾을 수 없습니다.");
    }
}
