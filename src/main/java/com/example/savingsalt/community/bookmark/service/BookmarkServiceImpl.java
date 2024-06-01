package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.community.bookmark.domain.BookmarkReqDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkResDto;
import com.example.savingsalt.community.bookmark.mapper.BookMarkMainMapper.BookmarkMapper;
import com.example.savingsalt.community.bookmark.repository.BookmarkRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public BookmarkDto bookmarkBoard(BookmarkReqDto bookmarkReqDto) {
        BoardEntity board = boardRepository.findById(bookmarkReqDto.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));

        MemberEntity member = memberRepository.findById(bookmarkReqDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        BookmarkDto bookmarkDto;
        if (bookmarkRepository.existsByBoardEntityAndMemberEntity(board, member)) {
            bookmarkRepository.deleteByBoardEntityAndMemberEntity(board, member);
            bookmarkDto = new BookmarkDto(null, board.getId(), member.getId());
        } else {
            BookmarkEntity bookmarkEntity = new BookmarkEntity(null, member, board);
            BookmarkEntity savedBookmarkEntity = bookmarkRepository.save(bookmarkEntity);
            bookmarkDto = bookmarkMapper.toDto(savedBookmarkEntity);
        }
        return bookmarkDto;
    }

    @Override
    public BookmarkResDto countBookmarks(Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        long count = bookmarkRepository.countByBoardEntity(board);
        return new BookmarkResDto(count);
    }

    @Override
    public List<BookmarkDto> getUserBookmarks(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        List<BookmarkEntity> bookmarks = bookmarkRepository.findAllByMemberEntity(member);
        return bookmarkMapper.toDto(bookmarks);
    }
}