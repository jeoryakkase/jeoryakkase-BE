package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkReqDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkResDto;
import java.util.List;

public interface BookmarkService {
    BookmarkDto bookmarkBoard(BookmarkReqDto bookmarkReqDto);
    BookmarkResDto countBookmarks(Long boardId);
    List<BookmarkDto> getUserBookmarks(Long memberId);
}
