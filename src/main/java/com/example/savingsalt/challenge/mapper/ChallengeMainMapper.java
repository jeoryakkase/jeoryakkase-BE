package com.example.savingsalt.challenge.mapper;

import com.example.savingsalt.challenge.domain.ChallengeDto;
import com.example.savingsalt.challenge.domain.ChallengeEntity;
import com.example.savingsalt.challenge.domain.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.MemberChallengeEntity;
import com.example.savingsalt.global.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface ChallengeMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberChallengeMapper extends
        EntityMapper<MemberChallengeEntity, MemberChallengeDto> {
    }

}
