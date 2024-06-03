package com.example.savingsalt.community.board.mapper;

import com.example.savingsalt.community.board.domain.dto.BoardTypeHofCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeHofReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteReadResDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-02T21:38:56+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Azul Systems, Inc.)"
)
@Component
public class BoardMainMapper$BoardMapperImpl implements BoardMainMapper.BoardMapper {

    @Override
    public BoardEntity toEntity(BoardTypeTipCreateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.title( dto.getTitle() );
        boardEntity.contents( dto.getContents() );
        List<String> list = dto.getImageUrls();
        if ( list != null ) {
            boardEntity.imageUrls( new ArrayList<String>( list ) );
        }

        return boardEntity.build();
    }

    @Override
    public BoardEntity toEntity(BoardTypeVoteCreateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.title( dto.getTitle() );
        boardEntity.contents( dto.getContents() );
        boardEntity.category( dto.getCategory() );

        return boardEntity.build();
    }

    @Override
    public BoardEntity toEntity(BoardTypeHofCreateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.contents( dto.getContents() );

        return boardEntity.build();
    }

    @Override
    public BoardTypeTipReadResDto toTipReadDto(BoardEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BoardTypeTipReadResDto.BoardTypeTipReadResDtoBuilder boardTypeTipReadResDto = BoardTypeTipReadResDto.builder();

        boardTypeTipReadResDto.id( entity.getId() );
        boardTypeTipReadResDto.nickname( entity.getNickname() );
        boardTypeTipReadResDto.title( entity.getTitle() );
        boardTypeTipReadResDto.contents( entity.getContents() );
        boardTypeTipReadResDto.totalLike( entity.getTotalLike() );
        boardTypeTipReadResDto.boardHits( entity.getBoardHits() );

        return boardTypeTipReadResDto.build();
    }

    @Override
    public BoardTypeVoteReadResDto toVoteReadDto(BoardEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BoardTypeVoteReadResDto.BoardTypeVoteReadResDtoBuilder boardTypeVoteReadResDto = BoardTypeVoteReadResDto.builder();

        boardTypeVoteReadResDto.id( entity.getId() );
        boardTypeVoteReadResDto.nickname( entity.getNickname() );
        boardTypeVoteReadResDto.title( entity.getTitle() );
        boardTypeVoteReadResDto.contents( entity.getContents() );
        boardTypeVoteReadResDto.boardHits( entity.getBoardHits() );

        return boardTypeVoteReadResDto.build();
    }

    @Override
    public BoardTypeHofReadResDto toHofReadDto(BoardEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BoardTypeHofReadResDto.BoardTypeHofReadResDtoBuilder boardTypeHofReadResDto = BoardTypeHofReadResDto.builder();

        boardTypeHofReadResDto.id( entity.getId() );
        boardTypeHofReadResDto.nickname( entity.getNickname() );
        boardTypeHofReadResDto.contents( entity.getContents() );
        boardTypeHofReadResDto.totalLike( entity.getTotalLike() );

        return boardTypeHofReadResDto.build();
    }
}
