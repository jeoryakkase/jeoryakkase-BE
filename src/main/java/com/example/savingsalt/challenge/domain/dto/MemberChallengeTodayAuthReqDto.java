package com.example.savingsalt.challenge.domain.dto;

import com.example.savingsalt.member.domain.MemberDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberChallengeTodayAuthReqDto {

    private MemberDto memberDto;

    private String progressDays;

    private LocalDateTime certificationDate;

    private String certificationImage;
}
