package com.example.savingsalt.community.poll.mapper;

import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.domain.PollChoiceEntity;
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
public class PollMainMapper$PollChoiceMapperImpl implements PollMainMapper.PollChoiceMapper {

    @Override
    public PollChoiceEntity toEntity(PollChoiceDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PollChoiceEntity.PollChoiceEntityBuilder pollChoiceEntity = PollChoiceEntity.builder();

        pollChoiceEntity.id( arg0.getId() );
        pollChoiceEntity.answer( arg0.getAnswer() );
        pollChoiceEntity.count( arg0.getCount() );

        return pollChoiceEntity.build();
    }

    @Override
    public List<PollChoiceEntity> toEntity(List<PollChoiceDto> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PollChoiceEntity> list = new ArrayList<PollChoiceEntity>( arg0.size() );
        for ( PollChoiceDto pollChoiceDto : arg0 ) {
            list.add( toEntity( pollChoiceDto ) );
        }

        return list;
    }

    @Override
    public PollChoiceDto toDto(PollChoiceEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PollChoiceDto.PollChoiceDtoBuilder pollChoiceDto = PollChoiceDto.builder();

        pollChoiceDto.id( arg0.getId() );
        pollChoiceDto.answer( arg0.getAnswer() );
        pollChoiceDto.count( arg0.getCount() );

        return pollChoiceDto.build();
    }

    @Override
    public List<PollChoiceDto> toDto(List<PollChoiceEntity> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PollChoiceDto> list = new ArrayList<PollChoiceDto>( arg0.size() );
        for ( PollChoiceEntity pollChoiceEntity : arg0 ) {
            list.add( toDto( pollChoiceEntity ) );
        }

        return list;
    }
}
