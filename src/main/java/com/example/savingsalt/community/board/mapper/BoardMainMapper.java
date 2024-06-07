package com.example.savingsalt.community.board.mapper;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface BoardMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface BoardMapper {
        BoardEntity toEntity(BoardTypeTipCreateReqDto dto);
        BoardEntity toEntity(BoardTypeVoteCreateReqDto dto);

        BoardTypeTipReadResDto toTipReadDto(BoardEntity entity);
        BoardTypeVoteReadResDto toVoteReadDto(BoardEntity entity);





    }

}
