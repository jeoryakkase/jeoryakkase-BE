package com.example.savingsalt.community.poll.mapper;

import com.example.savingsalt.community.poll.domain.PollChoiceDto;
import com.example.savingsalt.community.poll.domain.PollChoiceEntity;
import com.example.savingsalt.community.poll.domain.PollDto;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.community.poll.domain.PollResultDto;
import com.example.savingsalt.community.poll.domain.PollResultEntity;
import com.example.savingsalt.global.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface PollMainMapper {
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface PollMapper extends EntityMapper<PollEntity, PollDto> {
    }
}
