package com.example.savingsalt.goal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalStatus {
    PROCEEDING,     // 진행중
    COMPLETE,       // 완료
    FAILURE,        // 실패
    GIVE_UP         // 포기
}
