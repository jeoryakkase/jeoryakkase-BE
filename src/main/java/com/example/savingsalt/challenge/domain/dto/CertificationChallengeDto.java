package com.example.savingsalt.challenge.domain.dto;

import java.time.LocalDateTime;
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
public class CertificationChallengeDto {

    private Long id;

    private LocalDateTime certificationDate;

    private String content;

    private Integer saveMoney;

    private String nickname;

    private String profileImage;

    private Long representativeBadgeId;

    private List<CertificationChallengeImageDto> certificationChallengeImageDtos;
}
