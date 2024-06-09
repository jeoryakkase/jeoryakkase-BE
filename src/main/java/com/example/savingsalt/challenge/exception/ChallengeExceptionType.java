package com.example.savingsalt.challenge.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeExceptionType {
    NOT_FOUND_CHALLENGE("챌린지가 존재하지 않습니다."),
    NOT_FOUND_MEMBER_CHALLENGE("회원 챌린지가 존재하지 않습니다."),
    NOT_FOUND_CERTIFICATION_CHALLENGE("챌린지 인증 테이블이 존재하지 않습니다."),
    INVALID_CHALLENGE_TYPE("챌린지 목표 금액, 챌린지 목표 횟수 중 하나만 꼭 존재해야 됩니다."),
    NOT_FOUND_CHALLENGE_TYPE("라는 챌린지 유형은 존재하지 않습니다.(GOAL, COUNT 존재)."),
    INVALID_CHALLENGE_TERM("챌린지 기간은 '1일', '3일', '5일', '1주', '2주', '3주', '한 달' 중 하나로 설정되어 있어야 합니다."),
    ALREADY_SUCCEEDED("이미 완료된 챌린지 입니다.");

    private final String exceptionMessage;
}
