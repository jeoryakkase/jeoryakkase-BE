package com.example.savingsalt.challenge.domain;

import com.example.savingsalt.member.domain.MemberDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberChallengeTodayAuthReqDto {

    private MemberDto memberDto;

    private String progressDays;

    private LocalDateTime certificationDate;

    private String certificationImage;
}
