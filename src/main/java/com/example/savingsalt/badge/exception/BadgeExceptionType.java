package com.example.savingsalt.badge.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeExceptionType {
    BADGE_NOT_FOUND("해당 뱃지를 찾을 수 없습니다."),
    REPRESENTATIVE_BADGE_NOT_FOUND("대표 뱃지를 찾을 수 없습니다."),
    INVALID_REPRESENTATIVE_BADGE("해당 뱃지를 대표 뱃지로 등록 할 수 없습니다.");

    private final String exceptionMessage;

}
