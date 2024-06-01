package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-31T22:36:27+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Azul Systems, Inc.)"
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
        memberChallengeEntity.isTodayCertification( dto.getIsTodayCertification() );
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
        memberChallengeDto.isTodayCertification( entity.getIsTodayCertification() );
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

    @Override
    public MemberChallengeEntity toEntity(MemberChallengeCreateReqDto memberChallengeCreateReqDto) {
        if ( memberChallengeCreateReqDto == null ) {
            return null;
        }

        MemberChallengeEntity.MemberChallengeEntityBuilder memberChallengeEntity = MemberChallengeEntity.builder();

        memberChallengeEntity.startDate( memberChallengeCreateReqDto.getStartDate() );
        memberChallengeEntity.endDate( memberChallengeCreateReqDto.getEndDate() );
        memberChallengeEntity.certifyDate( memberChallengeCreateReqDto.getCertifyDate() );
        memberChallengeEntity.challengeStatus( memberChallengeCreateReqDto.getChallengeStatus() );
        memberChallengeEntity.isTodayCertification( memberChallengeCreateReqDto.getIsTodayCertification() );
        memberChallengeEntity.challengeConut( memberChallengeCreateReqDto.getChallengeConut() );
        memberChallengeEntity.challengeTry( memberChallengeCreateReqDto.getChallengeTry() );
        memberChallengeEntity.challengeComment( memberChallengeCreateReqDto.getChallengeComment() );
        memberChallengeEntity.saveMoney( memberChallengeCreateReqDto.getSaveMoney() );

        return memberChallengeEntity.build();
    }
}
