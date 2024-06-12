package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import java.util.List;

public interface BookmarkService {
    String bookmarkPost(String email, Long boardId);
    List<BookmarkDto> getBookmarks(String email);
}
