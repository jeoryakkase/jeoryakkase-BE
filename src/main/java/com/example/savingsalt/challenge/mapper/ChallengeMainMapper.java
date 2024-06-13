package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeImageDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeAbandonResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeImageEntity;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.global.EntityMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

public interface ChallengeMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface ChallengeMapper extends
        EntityMapper<ChallengeEntity, ChallengeDto> {

        @Mapping(source = "dto.challengeType", target = "challengeType")
        @Mapping(source = "dto.challengeDifficulty", target = "challengeDifficulty")
        ChallengeEntity toEntity(ChallengeCreateReqDto dto);

        @Mapping(source = "entity.badgeEntity.name", target = "badgeDto.name")
        @Mapping(source = "entity.badgeEntity.badgeDesc", target = "badgeDto.badgeDesc")
        @Mapping(source = "entity.badgeEntity.badgeImage", target = "badgeDto.badgeImage")
        @Mapping(source = "entity.badgeEntity.badgeType", target = "badgeDto.badgeType")
        @Mapping(source = "entity.badgeEntity.stroke", target = "badgeDto.stroke")
        @Mapping(source = "entity.badgeEntity.fill", target = "badgeDto.fill")
        ChallengeDto toDto(ChallengeEntity entity);

        @Mapping(source = "entity.badgeEntity.name", target = "badgeDto.name")
        @Mapping(source = "entity.badgeEntity.badgeDesc", target = "badgeDto.badgeDesc")
        @Mapping(source = "entity.badgeEntity.badgeImage", target = "badgeDto.badgeImage")
        @Mapping(source = "entity.badgeEntity.badgeType", target = "badgeDto.badgeType")
        @Mapping(source = "entity.badgeEntity.stroke", target = "badgeDto.stroke")
        @Mapping(source = "entity.badgeEntity.fill", target = "badgeDto.fill")
        ChallengeReadResDto toChallengesReadResDto(ChallengeEntity entity);

    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberChallengeMapper extends
        EntityMapper<MemberChallengeEntity, MemberChallengeDto> {

        MemberChallengeEntity toEntity(MemberChallengeEntity MemberChallengeDto);

        MemberChallengeCreateResDto toMemberChallengeCreateResDto(
            MemberChallengeEntity memberChallengeEntity);

        MemberChallengeEntity toEntity(MemberChallengeCreateResDto memberChallengeCreateResDto);

        MemberChallengeAbandonResDto toMemberChallengeAbandonResDto(
            MemberChallengeEntity memberChallengeEntity);
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface CertificationChallengeImageMapper {

        CertificationChallengeImageDto toDto(CertificationChallengeImageEntity entity);

        List<CertificationChallengeImageDto> toDtoList(
            List<CertificationChallengeImageEntity> entities);

        List<CertificationChallengeImageEntity> toEntityList(
            List<CertificationChallengeImageDto> dtos);
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = CertificationChallengeImageMapper.class)
    interface CertifiCationChallengeMapper {

        @Mapping(source = "certificationChallengeImageEntities", target = "certificationChallengeImageDtos")
        CertificationChallengeDto toDto(CertificationChallengeEntity entity);

        List<CertificationChallengeDto> toDtoList(List<CertificationChallengeEntity> entities);

        CertificationChallengeEntity toEntity(CertificationChallengeDto certificationChallengeDto);
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = CertifiCationChallengeMapper.class)
    interface MemberChallengeWithCertifyAndChallengeMapper extends
        EntityMapper<MemberChallengeEntity, MemberChallengeWithCertifyAndChallengeResDto> {

        @Mapping(source = "certificationChallengeEntities", target = "certificationChallengeDtos")
        @Mapping(source = "challengeEntity", target = "challengeDto")
        @Mapping(source = "challengeEntity.badgeEntity.name", target = "challengeDto.badgeDto.name")
        @Mapping(source = "challengeEntity.badgeEntity.badgeDesc", target = "challengeDto.badgeDto.badgeDesc")
        @Mapping(source = "challengeEntity.badgeEntity.badgeImage", target = "challengeDto.badgeDto.badgeImage")
        @Mapping(source = "challengeEntity.badgeEntity.badgeType", target = "challengeDto.badgeDto.badgeType")
        @Mapping(source = "challengeEntity.badgeEntity.stroke", target = "challengeDto.badgeDto.stroke")
        @Mapping(source = "challengeEntity.badgeEntity.fill", target = "challengeDto.badgeDto.fill")
        MemberChallengeWithCertifyAndChallengeResDto toDto(MemberChallengeEntity entity);

        List<MemberChallengeWithCertifyAndChallengeResDto> toDtoList(
            List<MemberChallengeEntity> entities);
    }

}
