package com.example.savingsalt.member.domain;

import java.util.List;
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
    private String savePurpose;
    private String profileImage;
    private List<Long> interests;
}
