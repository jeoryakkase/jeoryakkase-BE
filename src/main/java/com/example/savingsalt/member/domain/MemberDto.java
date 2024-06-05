package com.example.savingsalt.member.domain;

import com.example.savingsalt.member.enums.Gender;
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
    String gender;
    int income;
    int saving_goal;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
            .id(this.id)
            .email(this.email)
            .password(this.password)
            .nickname(this.nickname)
            .age(this.age)
            .gender(Gender.valueOf(this.gender))
            .income(this.income)
            .savingGoal(this.saving_goal)
            .build();
    }
}
