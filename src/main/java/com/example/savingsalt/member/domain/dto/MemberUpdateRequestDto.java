package com.example.savingsalt.member.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private String savePurpose;
    private List<Long> interests;
    private String about;
}
