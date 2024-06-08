package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import java.util.List;

public interface BookmarkService {
    String bookmarkPost(BookmarkDto bookmarkDto);
    List<BookmarkEntity> getBookmarks(Long memberId);
}
