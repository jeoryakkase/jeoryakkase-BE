package com.example.savingsalt.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String email;
    private String password;
    private String nickname;
    private int age;
    private int gender;
    private int income;
    private int savingGoal;
    private String profileImage;
}
