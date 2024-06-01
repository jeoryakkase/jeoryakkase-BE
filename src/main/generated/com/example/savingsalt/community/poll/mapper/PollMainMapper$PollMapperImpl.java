package com.example.savingsalt.community.poll.mapper;

import com.example.savingsalt.community.poll.domain.PollDto;
import com.example.savingsalt.community.poll.domain.PollEntity;
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
public class PollMainMapper$PollMapperImpl implements PollMainMapper.PollMapper {

    @Override
    public PollEntity toEntity(PollDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PollEntity.PollEntityBuilder pollEntity = PollEntity.builder();

        pollEntity.id( arg0.getId() );

        return pollEntity.build();
    }

    @Override
    public List<PollEntity> toEntity(List<PollDto> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PollEntity> list = new ArrayList<PollEntity>( arg0.size() );
        for ( PollDto pollDto : arg0 ) {
            list.add( toEntity( pollDto ) );
        }

        return list;
    }

    @Override
    public PollDto toDto(PollEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PollDto.PollDtoBuilder pollDto = PollDto.builder();

        pollDto.id( arg0.getId() );

        return pollDto.build();
    }

    @Override
    public List<PollDto> toDto(List<PollEntity> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PollDto> list = new ArrayList<PollDto>( arg0.size() );
        for ( PollEntity pollEntity : arg0 ) {
            list.add( toDto( pollEntity ) );
        }

        return list;
    }
}
