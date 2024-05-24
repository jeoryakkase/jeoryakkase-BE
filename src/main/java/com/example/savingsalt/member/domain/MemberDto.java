package com.example.savingsalt.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    long id;
    String email;
    String password;
    String nickname;
    int age;
    int gender;
    int income;
    int saving_goal;

    public Member toEntity() {
        return Member.builder()
            .id(this.id)
            .email(this.email)
            .password(this.password)
            .nickname(this.nickname)
            .age(this.age)
            .gender(this.gender)
            .income(this.income)
            .savingGoal(this.saving_goal)
            .build();
    }
}
