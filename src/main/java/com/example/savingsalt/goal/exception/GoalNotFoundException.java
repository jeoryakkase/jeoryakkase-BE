package com.example.savingsalt.goal.exception;

public class GoalNotFoundException extends RuntimeException {

    public GoalNotFoundException() {
        super("해당하는 목표를 찾을 수 없습니다.");
    }
}
