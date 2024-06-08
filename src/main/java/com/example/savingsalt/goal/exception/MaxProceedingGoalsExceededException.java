package com.example.savingsalt.goal.exception;

public class MaxProceedingGoalsExceededException extends RuntimeException {

    public MaxProceedingGoalsExceededException() {
        super("진행중인 목표는 최대 5개까지 생성할 수 있습니다.");
    }
}
