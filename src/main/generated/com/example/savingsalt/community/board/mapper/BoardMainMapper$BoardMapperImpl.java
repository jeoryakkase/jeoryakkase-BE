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
    date = "2024-06-01T20:31:07+0900",
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
        boardEntity.category( dto.getCategory() );
        List<String> list = dto.getImageUrls();
        if ( list != null ) {
            boardEntity.imageUrls( new ArrayList<String>( list ) );
        }

        return boardEntity.build();
    }

    @Override
    public BoardEntity toEntity(BoardTypeTipReadResDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.id( dto.getId() );
        boardEntity.nickname( dto.getNickname() );
        boardEntity.title( dto.getTitle() );
        boardEntity.contents( dto.getContents() );
        boardEntity.totalLike( dto.getTotalLike() );
        boardEntity.boardHits( dto.getBoardHits() );

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
    public BoardEntity toEntity(BoardTypeVoteReadResDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.id( dto.getId() );
        boardEntity.nickname( dto.getNickname() );
        boardEntity.title( dto.getTitle() );
        boardEntity.contents( dto.getContents() );
        boardEntity.boardHits( dto.getBoardHits() );

        return boardEntity.build();
    }

    @Override
    public BoardEntity toEntity(BoardTypeHofCreateReqDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.title( dto.getTitle() );
        boardEntity.contents( dto.getContents() );

        return boardEntity.build();
    }

    @Override
    public BoardEntity toEntity(BoardTypeHofReadResDto dto) {
        if ( dto == null ) {
            return null;
        }

        BoardEntity.BoardEntityBuilder boardEntity = BoardEntity.builder();

        boardEntity.id( dto.getId() );
        boardEntity.nickname( dto.getNickname() );
        boardEntity.title( dto.getTitle() );
        boardEntity.contents( dto.getContents() );
        boardEntity.totalLike( dto.getTotalLike() );

        return boardEntity.build();
    }
}
