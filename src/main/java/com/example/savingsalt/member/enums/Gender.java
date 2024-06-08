package com.example.savingsalt.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("GENDER_MALE"),
    FEMALE("GENDER_FEAMLE");

    private final String key;
}
