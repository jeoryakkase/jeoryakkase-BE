package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.community.bookmark.domain.BookmarkReqDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkResDto;
import com.example.savingsalt.community.bookmark.repository.BookmarkRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
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

    @Override
    @Transactional
    public void bookmarkBoard(BookmarkReqDto bookmarkReqDto) {
        BoardEntity board = boardRepository.findById(bookmarkReqDto.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        MemberEntity member = memberRepository.findById(bookmarkReqDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        if (bookmarkRepository.existsByBoardEntityAndMemberEntity(board, member)) {
            bookmarkRepository.deleteByBoardEntityAndMemberEntity(board, member);
        } else {
            BookmarkEntity bookmark = new BookmarkEntity(null, member, board);
            bookmarkRepository.save(bookmark);
        }
    }

    @Override
    public BookmarkResDto countBookmarks(Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        long count = bookmarkRepository.countByBoardEntity(board);
        return new BookmarkResDto(count);
    }
}