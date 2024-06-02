package com.example.savingsalt.community.poll.mapper;

import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollResultEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-01T18:53:37+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class PollMainMapper$PollResultMapperImpl implements PollMainMapper.PollResultMapper {

    @Override
    public PollResultEntity toEntity(PollResultDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PollResultEntity pollResultEntity = new PollResultEntity();

        return pollResultEntity;
    }

    @Override
    public List<PollResultEntity> toEntity(List<PollResultDto> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PollResultEntity> list = new ArrayList<PollResultEntity>( arg0.size() );
        for ( PollResultDto pollResultDto : arg0 ) {
            list.add( toEntity( pollResultDto ) );
        }

        return list;
    }

    @Override
    public PollResultDto toDto(PollResultEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PollResultDto.PollResultDtoBuilder pollResultDto = PollResultDto.builder();

        pollResultDto.id( arg0.getId() );

        return pollResultDto.build();
    }

    @Override
    public List<PollResultDto> toDto(List<PollResultEntity> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PollResultDto> list = new ArrayList<PollResultDto>( arg0.size() );
        for ( PollResultEntity pollResultEntity : arg0 ) {
            list.add( toDto( pollResultEntity ) );
        }

        return list;
    }
}
