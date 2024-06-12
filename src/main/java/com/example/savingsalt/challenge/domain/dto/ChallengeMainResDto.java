package com.example.savingsalt.challenge.domain.dto;

import java.util.ArrayList;
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
public class ChallengeMainResDto {

    private List<MemberChallengeJoinResDto> memberChallengesJoinResDto = new ArrayList<>();
    private List<ChallengeReadResDto> challengesReadResDto = new ArrayList<>();
    private List<ChallengeReadResDto> challengesPopularityReadResDto = new ArrayList<>();


}
