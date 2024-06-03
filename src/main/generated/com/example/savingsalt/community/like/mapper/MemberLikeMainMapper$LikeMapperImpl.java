package com.example.savingsalt.community.like.mapper;

import com.example.savingsalt.community.like.domain.MemberLikeDto;
import com.example.savingsalt.community.like.domain.MemberLikeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-01T18:53:38+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class MemberLikeMainMapper$LikeMapperImpl implements MemberLikeMainMapper.LikeMapper {

    @Override
    public MemberLikeEntity toEntity(MemberLikeDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        MemberLikeEntity memberLikeEntity = new MemberLikeEntity();

        return memberLikeEntity;
    }

    @Override
    public List<MemberLikeEntity> toEntity(List<MemberLikeDto> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<MemberLikeEntity> list = new ArrayList<MemberLikeEntity>( arg0.size() );
        for ( MemberLikeDto memberLikeDto : arg0 ) {
            list.add( toEntity( memberLikeDto ) );
        }

        return list;
    }

    @Override
    public MemberLikeDto toDto(MemberLikeEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        MemberLikeDto memberLikeDto = new MemberLikeDto();

        return memberLikeDto;
    }

    @Override
    public List<MemberLikeDto> toDto(List<MemberLikeEntity> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<MemberLikeDto> list = new ArrayList<MemberLikeDto>( arg0.size() );
        for ( MemberLikeEntity memberLikeEntity : arg0 ) {
            list.add( toDto( memberLikeEntity ) );
        }

        return list;
    }
}
