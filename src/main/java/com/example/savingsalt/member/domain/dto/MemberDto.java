package com.example.savingsalt.member.domain.dto;

import com.example.savingsalt.member.enums.Gender;
import java.util.List;
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

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private int age;
    private String gender;
    private int income;
    private String savePurpose;
    private List<Long> interests;
    private String about;
    private Long representativeBadgeId;
}
