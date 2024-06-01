package com.example.savingsalt.badge.domain.dto;

import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberGoalBadgeCreateReqDto {

    private BadgeEntity badgeEntity;

    private MemberEntity memberEntity;
}
