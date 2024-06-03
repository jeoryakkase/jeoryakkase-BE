package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-02T21:38:56+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Azul Systems, Inc.)"
)
@Component
public class ChallengeMainMapper$ChallengeMapperImpl implements ChallengeMainMapper.ChallengeMapper {

    @Override
    public ChallengeEntity toEntity(ChallengeDto dto) {
        if ( dto == null ) {
            return null;
        }

        ChallengeEntity.ChallengeEntityBuilder challengeEntity = ChallengeEntity.builder();

        challengeEntity.challengeTitle( dto.getChallengeTitle() );
        challengeEntity.challengeDesc( dto.getChallengeDesc() );
        challengeEntity.challengeGoal( dto.getChallengeGoal() );
        challengeEntity.challengeCount( dto.getChallengeCount() );
        challengeEntity.challengeType( dto.getChallengeType() );
        challengeEntity.challengeTerm( dto.getChallengeTerm() );
        challengeEntity.challengeDifficulty( dto.getChallengeDifficulty() );
        challengeEntity.authContent( dto.getAuthContent() );

        return challengeEntity.build();
    }

    @Override
    public List<ChallengeEntity> toEntity(List<ChallengeDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<ChallengeEntity> list = new ArrayList<ChallengeEntity>( dtos.size() );
        for ( ChallengeDto challengeDto : dtos ) {
            list.add( toEntity( challengeDto ) );
        }

        return list;
    }

    @Override
    public ChallengeDto toDto(ChallengeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ChallengeDto.ChallengeDtoBuilder challengeDto = ChallengeDto.builder();

        challengeDto.challengeTitle( entity.getChallengeTitle() );
        challengeDto.challengeDesc( entity.getChallengeDesc() );
        challengeDto.challengeGoal( entity.getChallengeGoal() );
        challengeDto.challengeCount( entity.getChallengeCount() );
        challengeDto.challengeType( entity.getChallengeType() );
        challengeDto.challengeTerm( entity.getChallengeTerm() );
        challengeDto.challengeDifficulty( entity.getChallengeDifficulty() );
        challengeDto.authContent( entity.getAuthContent() );

        return challengeDto.build();
    }

    @Override
    public List<ChallengeDto> toDto(List<ChallengeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ChallengeDto> list = new ArrayList<ChallengeDto>( entities.size() );
        for ( ChallengeEntity challengeEntity : entities ) {
            list.add( toDto( challengeEntity ) );
        }

        return list;
    }

    @Override
    public ChallengeEntity toEntity(ChallengeCreateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        ChallengeEntity.ChallengeEntityBuilder challengeEntity = ChallengeEntity.builder();

        challengeEntity.challengeTitle( dto.getChallengeTitle() );
        challengeEntity.challengeDesc( dto.getChallengeDesc() );
        challengeEntity.challengeGoal( dto.getChallengeGoal() );
        challengeEntity.challengeCount( dto.getChallengeCount() );
        challengeEntity.challengeType( dto.getChallengeType() );
        challengeEntity.challengeTerm( dto.getChallengeTerm() );
        challengeEntity.challengeDifficulty( dto.getChallengeDifficulty() );
        challengeEntity.authContent( dto.getAuthContent() );

        return challengeEntity.build();
    }

    @Override
    public ChallengeEntity toEntity(ChallengeUpdateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        ChallengeEntity.ChallengeEntityBuilder challengeEntity = ChallengeEntity.builder();

        challengeEntity.challengeTitle( dto.getChallengeTitle() );
        challengeEntity.challengeDesc( dto.getChallengeDesc() );
        challengeEntity.challengeGoal( dto.getChallengeGoal() );
        challengeEntity.challengeCount( dto.getChallengeCount() );
        challengeEntity.challengeType( dto.getChallengeType() );
        challengeEntity.challengeTerm( dto.getChallengeTerm() );
        challengeEntity.challengeDifficulty( dto.getChallengeDifficulty() );
        challengeEntity.authContent( dto.getAuthContent() );

        return challengeEntity.build();
    }

    @Override
    public ChallengeReadResDto toChallengesReadResDto(ChallengeEntity entities) {
        if ( entities == null ) {
            return null;
        }

        ChallengeReadResDto.ChallengeReadResDtoBuilder challengeReadResDto = ChallengeReadResDto.builder();

        challengeReadResDto.challengeTitle( entities.getChallengeTitle() );
        challengeReadResDto.challengeType( entities.getChallengeType() );
        challengeReadResDto.challengeTerm( entities.getChallengeTerm() );
        challengeReadResDto.challengeCount( entities.getChallengeCount() );

        return challengeReadResDto.build();
    }
}
