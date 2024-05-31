package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
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
public class ChallengeMainMapper$CertificationChallengeMapperImpl implements ChallengeMainMapper.CertificationChallengeMapper {

    @Override
    public CertificationChallengeEntity toEntity(CertificationChallengeDto dto) {
        if ( dto == null ) {
            return null;
        }

        CertificationChallengeEntity.CertificationChallengeEntityBuilder certificationChallengeEntity = CertificationChallengeEntity.builder();

        certificationChallengeEntity.certificationDate( dto.getCertificationDate() );
        certificationChallengeEntity.challengeImg( dto.getChallengeImg() );

        return certificationChallengeEntity.build();
    }

    @Override
    public List<CertificationChallengeEntity> toEntity(List<CertificationChallengeDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<CertificationChallengeEntity> list = new ArrayList<CertificationChallengeEntity>( dtos.size() );
        for ( CertificationChallengeDto certificationChallengeDto : dtos ) {
            list.add( toEntity( certificationChallengeDto ) );
        }

        return list;
    }

    @Override
    public CertificationChallengeDto toDto(CertificationChallengeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CertificationChallengeDto.CertificationChallengeDtoBuilder certificationChallengeDto = CertificationChallengeDto.builder();

        certificationChallengeDto.certificationDate( entity.getCertificationDate() );
        certificationChallengeDto.challengeImg( entity.getChallengeImg() );

        return certificationChallengeDto.build();
    }

    @Override
    public List<CertificationChallengeDto> toDto(List<CertificationChallengeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CertificationChallengeDto> list = new ArrayList<CertificationChallengeDto>( entities.size() );
        for ( CertificationChallengeEntity certificationChallengeEntity : entities ) {
            list.add( toDto( certificationChallengeEntity ) );
        }

        return list;
    }
}
