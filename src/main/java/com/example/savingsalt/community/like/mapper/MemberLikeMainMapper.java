package com.example.savingsalt.community.like.mapper;

import com.example.savingsalt.community.like.domain.MemberLikeDto;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import com.example.savingsalt.global.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface MemberLikeMainMapper {
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface LikeMapper extends EntityMapper<MemberLikeEntity, MemberLikeDto> {
    }
}
