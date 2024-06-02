package com.example.savingsalt.community.bookmark.mapper;

import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
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
public class BookMarkMainMapper$BookmarkMapperImpl implements BookMarkMainMapper.BookmarkMapper {

    @Override
    public BookmarkEntity toEntity(BookmarkDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BookmarkEntity bookmarkEntity = new BookmarkEntity();

        return bookmarkEntity;
    }

    @Override
    public List<BookmarkEntity> toEntity(List<BookmarkDto> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BookmarkEntity> list = new ArrayList<BookmarkEntity>( arg0.size() );
        for ( BookmarkDto bookmarkDto : arg0 ) {
            list.add( toEntity( bookmarkDto ) );
        }

        return list;
    }

    @Override
    public BookmarkDto toDto(BookmarkEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BookmarkDto bookmarkDto = new BookmarkDto();

        return bookmarkDto;
    }

    @Override
    public List<BookmarkDto> toDto(List<BookmarkEntity> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BookmarkDto> list = new ArrayList<BookmarkDto>( arg0.size() );
        for ( BookmarkEntity bookmarkEntity : arg0 ) {
            list.add( toDto( bookmarkEntity ) );
        }

        return list;
    }
}
