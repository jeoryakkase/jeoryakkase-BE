package com.example.savingsalt.goal.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("해당 목표에 대한 권한이 없습니다.");
    }
}
