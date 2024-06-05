package com.example.savingsalt.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequestDto {

    private String email;
    private String password;
    private String nickname;
    private int age;
    private String gender;
    private int income;
    private int savingGoal;
    private String profileImage;
}
