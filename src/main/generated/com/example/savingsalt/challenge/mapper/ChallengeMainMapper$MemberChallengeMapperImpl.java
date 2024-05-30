package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.MemberChallengeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-30T19:17:15+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class ChallengeMainMapper$MemberChallengeMapperImpl implements ChallengeMainMapper.MemberChallengeMapper {

    @Override
    public MemberChallengeEntity toEntity(MemberChallengeDto dto) {
        if ( dto == null ) {
            return null;
        }

        MemberChallengeEntity.MemberChallengeEntityBuilder memberChallengeEntity = MemberChallengeEntity.builder();

        memberChallengeEntity.startDate( dto.getStartDate() );
        memberChallengeEntity.endDate( dto.getEndDate() );
        memberChallengeEntity.certifyDate( dto.getCertifyDate() );
        memberChallengeEntity.challengeStatus( dto.getChallengeStatus() );
        memberChallengeEntity.challengeConut( dto.getChallengeConut() );
        memberChallengeEntity.challengeTry( dto.getChallengeTry() );
        memberChallengeEntity.challengeComment( dto.getChallengeComment() );
        memberChallengeEntity.saveMoney( dto.getSaveMoney() );

        return memberChallengeEntity.build();
    }

    @Override
    public List<MemberChallengeEntity> toEntity(List<MemberChallengeDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<MemberChallengeEntity> list = new ArrayList<MemberChallengeEntity>( dtos.size() );
        for ( MemberChallengeDto memberChallengeDto : dtos ) {
            list.add( toEntity( memberChallengeDto ) );
        }

        return list;
    }

    @Override
    public MemberChallengeDto toDto(MemberChallengeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MemberChallengeDto.MemberChallengeDtoBuilder memberChallengeDto = MemberChallengeDto.builder();

        memberChallengeDto.startDate( entity.getStartDate() );
        memberChallengeDto.endDate( entity.getEndDate() );
        memberChallengeDto.certifyDate( entity.getCertifyDate() );
        memberChallengeDto.challengeStatus( entity.getChallengeStatus() );
        memberChallengeDto.challengeConut( entity.getChallengeConut() );
        memberChallengeDto.challengeTry( entity.getChallengeTry() );
        memberChallengeDto.challengeComment( entity.getChallengeComment() );
        memberChallengeDto.saveMoney( entity.getSaveMoney() );

        return memberChallengeDto.build();
    }

    @Override
    public List<MemberChallengeDto> toDto(List<MemberChallengeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MemberChallengeDto> list = new ArrayList<MemberChallengeDto>( entities.size() );
        for ( MemberChallengeEntity memberChallengeEntity : entities ) {
            list.add( toDto( memberChallengeEntity ) );
        }

        return list;
    }
}
