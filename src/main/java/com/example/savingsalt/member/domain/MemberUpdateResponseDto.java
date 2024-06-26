package com.example.savingsalt.member.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateResponseDto {

    private String email;
    private String nickname;
    private int age;
    private String gender;
    private int income;
    private String savePurpose;
    private String profileImage;
    private List<Long> interests;
    private String about;
}
