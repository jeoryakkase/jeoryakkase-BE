package com.example.savingsalt.member.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SignupRequestDto {

    private String email;
    private String password;
    private String nickname;
    private int age;
    private String gender;
    private int income;
    private String savePurpose;
    private List<Long> interests;
    private String about;
}
