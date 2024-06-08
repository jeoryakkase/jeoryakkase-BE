package com.example.savingsalt.challenge.domain.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class CertificationChallengeReqDto {

    private String content;

    private List<String> imageUrls;

    private Integer saveMoney;
}
