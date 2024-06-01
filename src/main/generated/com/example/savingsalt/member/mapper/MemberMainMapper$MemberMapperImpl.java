package com.example.savingsalt.member.mapper;

import com.example.savingsalt.member.domain.MemberDto;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
<<<<<<< HEAD
    date = "2024-06-01T01:38:41+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
=======
    date = "2024-05-31T22:36:27+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Azul Systems, Inc.)"
>>>>>>> 91192aacc92e3dd0b7e1dbfe2105a553de75176b
)
@Component
public class MemberMainMapper$MemberMapperImpl implements MemberMainMapper.MemberMapper {

    @Override
    public MemberEntity toEntity(MemberDto dto) {
        if ( dto == null ) {
            return null;
        }

        MemberEntity.MemberEntityBuilder memberEntity = MemberEntity.builder();

        memberEntity.id( dto.getId() );
        memberEntity.email( dto.getEmail() );
        memberEntity.password( dto.getPassword() );
        memberEntity.nickname( dto.getNickname() );
        memberEntity.age( dto.getAge() );
        memberEntity.gender( dto.getGender() );
        memberEntity.income( dto.getIncome() );

        return memberEntity.build();
    }

    @Override
    public List<MemberEntity> toEntity(List<MemberDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<MemberEntity> list = new ArrayList<MemberEntity>( dtos.size() );
        for ( MemberDto memberDto : dtos ) {
            list.add( toEntity( memberDto ) );
        }

        return list;
    }

    @Override
    public MemberDto toDto(MemberEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MemberDto.MemberDtoBuilder memberDto = MemberDto.builder();

        if ( entity.getId() != null ) {
            memberDto.id( entity.getId() );
        }
        memberDto.email( entity.getEmail() );
        memberDto.password( entity.getPassword() );
        memberDto.nickname( entity.getNickname() );
        memberDto.age( entity.getAge() );
        memberDto.gender( entity.getGender() );
        memberDto.income( entity.getIncome() );

        return memberDto.build();
    }

    @Override
    public List<MemberDto> toDto(List<MemberEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MemberDto> list = new ArrayList<MemberDto>( entities.size() );
        for ( MemberEntity memberEntity : entities ) {
            list.add( toDto( memberEntity ) );
        }

        return list;
    }
}
