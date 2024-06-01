package com.example.savingsalt.community.bookmark.mapper;

import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.global.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

public interface BookMarkMainMapper {
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface BookmarkMapper extends EntityMapper<BookmarkEntity, BookmarkDto> {
    }
}
