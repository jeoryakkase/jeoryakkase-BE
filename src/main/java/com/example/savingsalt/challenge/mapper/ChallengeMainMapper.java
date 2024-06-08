package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeImageDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
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

        @Mapping(source = "dto.challengeDifficulty", target = "challengeDifficulty")
        ChallengeEntity toEntity(ChallengeCreateReqDto dto);

        ChallengeEntity toEntity(ChallengeUpdateReqDto dto);

        @Mapping(source = "entity.badgeEntity.name", target = "badgeDto.name")
        @Mapping(source = "entity.badgeEntity.badgeDesc", target = "badgeDto.badgeDesc")
        @Mapping(source = "entity.badgeEntity.badgeImage", target = "badgeDto.badgeImage")
        @Mapping(source = "entity.badgeEntity.badgeType", target = "badgeDto.badgeType")
        ChallengeDto toDto(ChallengeEntity entity);

        @Mapping(source = "entity.badgeEntity.name", target = "badgeDto.name")
        @Mapping(source = "entity.badgeEntity.badgeDesc", target = "badgeDto.badgeDesc")
        @Mapping(source = "entity.badgeEntity.badgeImage", target = "badgeDto.badgeImage")
        @Mapping(source = "entity.badgeEntity.badgeType", target = "badgeDto.badgeType")
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
    interface CertificationChallengeMapper extends
        EntityMapper<CertificationChallengeEntity, CertificationChallengeDto> {

        CertificationChallengeEntity toEntity(
            CertificationChallengeReqDto certificationChallengeReqDto);
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberChallengeWithCertifyAndChallengeMapper extends
        EntityMapper<MemberChallengeEntity, MemberChallengeWithCertifyAndChallengeResDto> {

        @Mapping(source = "certificationChallengeEntities", target = "certificationChallengeDtos")
        @Mapping(source = "challengeEntity", target = "challengeDto")
        @Mapping(source = "challengeEntity.badgeEntity.name", target = "challengeDto.badgeDto.name")
        @Mapping(source = "challengeEntity.badgeEntity.badgeDesc", target = "challengeDto.badgeDto.badgeDesc")
        @Mapping(source = "challengeEntity.badgeEntity.badgeImage", target = "challengeDto.badgeDto.badgeImage")
        @Mapping(source = "challengeEntity.badgeEntity.badgeType", target = "challengeDto.badgeDto.badgeType")
        MemberChallengeWithCertifyAndChallengeResDto toDto(
            MemberChallengeEntity memberChallengeEntity);

        @Mapping(source = "certificationChallengeEntities", target = "certificationChallengeDtos")
        @Mapping(source = "challengeEntity", target = "challengeDto")
        List<MemberChallengeWithCertifyAndChallengeResDto> toDtoList(
            List<MemberChallengeEntity> memberChallengeEntities);

    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface CertificationChallengeImageMapper extends EntityMapper<CertificationChallengeImageEntity, CertificationChallengeImageDto> {
    }

}
