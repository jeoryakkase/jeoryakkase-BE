package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCompleteReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.global.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

public interface ChallengeMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface ChallengeMapper extends
        EntityMapper<ChallengeEntity, ChallengeDto> {

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

        MemberChallengeCreateReqDto toMemberChallengeCreateReqDto(
            MemberChallengeEntity memberChallengeEntity);

        MemberChallengeEntity toEntity(MemberChallengeCreateReqDto memberChallengeCreateReqDto);

        MemberChallengeCompleteReqDto toMemberChallengeCompleteReqDto(
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
        MemberChallengeWithCertifyAndChallengeResDto toDto(
            MemberChallengeEntity memberChallengeEntity);

        @Mapping(source = "certificationChallengeDtos", target = "certificationChallengeEntities")
        @Mapping(source = "challengeDto", target = "challengeEntity")
        MemberChallengeEntity toEntity(
            MemberChallengeWithCertifyAndChallengeResDto memberChallengeWithCertifyAndChallengeResDto);
    }

}
