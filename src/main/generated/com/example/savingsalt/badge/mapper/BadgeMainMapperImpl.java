package com.example.savingsalt.badge.mapper;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-07T16:50:17+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class BadgeMainMapperImpl implements BadgeMainMapper {

    @Override
    public BadgeEntity toEntity(BadgeDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BadgeEntity.BadgeEntityBuilder badgeEntity = BadgeEntity.builder();

        badgeEntity.name( arg0.getName() );
        badgeEntity.badgeImage( arg0.getBadgeImage() );
        badgeEntity.badgeDesc( arg0.getBadgeDesc() );
        badgeEntity.badgeType( arg0.getBadgeType() );

        return badgeEntity.build();
    }

    @Override
    public List<BadgeEntity> toEntity(List<BadgeDto> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BadgeEntity> list = new ArrayList<BadgeEntity>( arg0.size() );
        for ( BadgeDto badgeDto : arg0 ) {
            list.add( toEntity( badgeDto ) );
        }

        return list;
    }

    @Override
    public BadgeDto toDto(BadgeEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BadgeDto.BadgeDtoBuilder badgeDto = BadgeDto.builder();

        badgeDto.name( arg0.getName() );
        badgeDto.badgeImage( arg0.getBadgeImage() );
        badgeDto.badgeDesc( arg0.getBadgeDesc() );
        badgeDto.badgeType( arg0.getBadgeType() );

        return badgeDto.build();
    }

    @Override
    public List<BadgeDto> toDto(List<BadgeEntity> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BadgeDto> list = new ArrayList<BadgeDto>( arg0.size() );
        for ( BadgeEntity badgeEntity : arg0 ) {
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
}
