package com.example.savingsalt.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2SignupRequestDto {

    private String email;
    private String nickname;
    private int age;
    private String gender;
    private int income;
    private int savingGoal;
    private String profileImage;
}
