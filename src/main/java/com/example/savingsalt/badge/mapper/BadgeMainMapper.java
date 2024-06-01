package com.example.savingsalt.badge.mapper;


import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.dto.MemberGoalBadgeResDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.domain.entity.MemberGoalBadgeEntity;
import com.example.savingsalt.global.EntityMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BadgeMainMapper extends EntityMapper<BadgeEntity, BadgeDto> {

    BadgeEntity toEntity(BadgeCreateReqDto dto);

    MemberChallengeBadgeResDto toMemberChallengeBadgeResDto(BadgeEntity entity);

    List<MemberGoalBadgeResDto> toMemberGoalBadgeResDto(List<MemberGoalBadgeEntity> entities);

    BadgeDto toDto(MemberGoalBadgeEntity entity);

}
