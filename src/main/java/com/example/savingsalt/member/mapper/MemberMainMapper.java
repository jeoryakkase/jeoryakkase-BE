package com.example.savingsalt.member.mapper;

import com.example.savingsalt.global.EntityMapper;
import com.example.savingsalt.member.domain.MemberDto;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.MemberUpdateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface MemberMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberMapper extends EntityMapper<MemberEntity, MemberDto> {

    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberUpdateMapper extends EntityMapper<MemberEntity, MemberUpdateResponseDto> {

    }
}
