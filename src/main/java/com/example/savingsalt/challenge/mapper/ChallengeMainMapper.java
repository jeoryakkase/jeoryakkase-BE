package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeReadResDto;
import com.example.savingsalt.challenge.domain.dto.ChallengeUpdateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.ChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.global.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface ChallengeMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface ChallengeMapper extends
        EntityMapper<ChallengeEntity, ChallengeDto> {

        ChallengeEntity toEntity(ChallengeCreateReqDto dto);

        ChallengeEntity toEntity(ChallengeUpdateReqDto dto);

        ChallengeReadResDto toChallengesReadResDto(ChallengeEntity entities);

    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberChallengeMapper extends
        EntityMapper<MemberChallengeEntity, MemberChallengeDto> {

        MemberChallengeEntity toEntity(MemberChallengeCreateReqDto memberChallengeCreateReqDto);

        MemberChallengeCreateReqDto toMemberChallengeCreateReqDto(MemberChallengeEntity memberChallenge);
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface CertificationChallengeMapper extends
        EntityMapper<CertificationChallengeEntity, CertificationChallengeDto> {

        CertificationChallengeEntity toEntity(CertificationChallengeReqDto certificationChallengeReqDto);
    }

}
