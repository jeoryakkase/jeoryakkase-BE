package com.example.savingsalt.badge.mapper;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.dto.MemberGoalBadgeResDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.domain.entity.MemberGoalBadgeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-01T01:38:42+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class BadgeMainMapperImpl implements BadgeMainMapper {

    @Override
    public BadgeEntity toEntity(BadgeDto dto) {
        if ( dto == null ) {
            return null;
        }

        BadgeEntity.BadgeEntityBuilder badgeEntity = BadgeEntity.builder();

        badgeEntity.name( dto.getName() );
        badgeEntity.badgeImage( dto.getBadgeImage() );
        badgeEntity.badgeDesc( dto.getBadgeDesc() );
        badgeEntity.badgeType( dto.getBadgeType() );

        return badgeEntity.build();
    }

    @Override
    public List<BadgeEntity> toEntity(List<BadgeDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<BadgeEntity> list = new ArrayList<BadgeEntity>( dtos.size() );
        for ( BadgeDto badgeDto : dtos ) {
            list.add( toEntity( badgeDto ) );
        }

        return list;
    }

    @Override
    public BadgeDto toDto(BadgeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BadgeDto.BadgeDtoBuilder badgeDto = BadgeDto.builder();

        badgeDto.name( entity.getName() );
        badgeDto.badgeImage( entity.getBadgeImage() );
        badgeDto.badgeDesc( entity.getBadgeDesc() );
        badgeDto.badgeType( entity.getBadgeType() );

        return badgeDto.build();
    }

    @Override
    public List<BadgeDto> toDto(List<BadgeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BadgeDto> list = new ArrayList<BadgeDto>( entities.size() );
        for ( BadgeEntity badgeEntity : entities ) {
            list.add( toDto( badgeEntity ) );
        }

        return list;
    }

    @Override
    public BadgeEntity toEntity(BadgeCreateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        BadgeEntity.BadgeEntityBuilder badgeEntity = BadgeEntity.builder();

        badgeEntity.name( dto.getName() );
        badgeEntity.badgeImage( dto.getBadgeImage() );
        badgeEntity.badgeDesc( dto.getBadgeDesc() );
        badgeEntity.badgeType( dto.getBadgeType() );

        return badgeEntity.build();
    }

    @Override
    public MemberChallengeBadgeResDto toMemberChallengeBadgeResDto(BadgeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MemberChallengeBadgeResDto.MemberChallengeBadgeResDtoBuilder memberChallengeBadgeResDto = MemberChallengeBadgeResDto.builder();

        memberChallengeBadgeResDto.name( entity.getName() );
        memberChallengeBadgeResDto.badgeDesc( entity.getBadgeDesc() );
        memberChallengeBadgeResDto.badgeImage( entity.getBadgeImage() );

        return memberChallengeBadgeResDto.build();
    }

    @Override
    public List<MemberGoalBadgeResDto> toMemberGoalBadgeResDto(List<MemberGoalBadgeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MemberGoalBadgeResDto> list = new ArrayList<MemberGoalBadgeResDto>( entities.size() );
        for ( MemberGoalBadgeEntity memberGoalBadgeEntity : entities ) {
            list.add( memberGoalBadgeEntityToMemberGoalBadgeResDto( memberGoalBadgeEntity ) );
        }

        return list;
    }

    @Override
    public BadgeDto toDto(MemberGoalBadgeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BadgeDto.BadgeDtoBuilder badgeDto = BadgeDto.builder();

        return badgeDto.build();
    }

    protected MemberGoalBadgeResDto memberGoalBadgeEntityToMemberGoalBadgeResDto(MemberGoalBadgeEntity memberGoalBadgeEntity) {
        if ( memberGoalBadgeEntity == null ) {
            return null;
        }

        MemberGoalBadgeResDto.MemberGoalBadgeResDtoBuilder memberGoalBadgeResDto = MemberGoalBadgeResDto.builder();

        return memberGoalBadgeResDto.build();
    }
}
