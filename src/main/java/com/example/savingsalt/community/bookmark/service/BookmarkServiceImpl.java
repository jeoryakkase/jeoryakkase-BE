package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.community.bookmark.repository.BookmarkRepository;
import com.example.savingsalt.community.bookmark.exception.BookmarkException.BoardNotFoundException;
import com.example.savingsalt.community.bookmark.exception.BookmarkException.MemberNotFoundException;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
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
    public String bookmarkPost(String email, Long boardId) {
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElseThrow(
            MemberNotFoundException::new);

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(
            BoardNotFoundException::new);

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByMemberEntityAndBoardEntity(memberEntity, boardEntity);
        if (bookmarkEntity == null) {
            bookmarkEntity = BookmarkEntity.builder()
                .memberEntity(memberEntity)
                .boardEntity(boardEntity)
                .build();
            bookmarkRepository.save(bookmarkEntity);
            return "북마크 완료";
        } else {
            bookmarkRepository.delete(bookmarkEntity);
            return "북마크 취소";
        }
    }

    @Override
    public List<BookmarkDto> getBookmarks(String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElseThrow(
            MemberNotFoundException::new);
        List<BookmarkEntity> bookmarks = bookmarkRepository.findAllByMemberEntity(memberEntity);

        return bookmarks.stream()
            .map(bookmark -> BookmarkDto.builder()
                .boardId(bookmark.getBoardEntity().getId())
                .build())
            .collect(Collectors.toList());
    }
}