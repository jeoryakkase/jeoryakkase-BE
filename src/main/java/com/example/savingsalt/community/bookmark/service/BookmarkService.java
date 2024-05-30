package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.bookmark.domain.BookmarkReqDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkResDto;

public interface BookmarkService {
    void bookmarkBoard(BookmarkReqDto bookmarkReqDto);
    BookmarkResDto countBookmarks(Long boardId);
}
