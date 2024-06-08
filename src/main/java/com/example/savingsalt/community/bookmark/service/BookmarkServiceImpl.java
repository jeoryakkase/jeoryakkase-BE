package com.example.savingsalt.community.bookmark.service;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.bookmark.domain.BookmarkDto;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.community.bookmark.repository.BookmarkRepository;
import com.example.savingsalt.community.bookmark.exception.BookmarkException.BoardNotFoundException;
import com.example.savingsalt.community.bookmark.exception.BookmarkException.MemberNotFoundException;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
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
    public String bookmarkPost(BookmarkDto bookmarkDto) {
        Long memberId = bookmarkDto.getMemberId();
        Long boardId = bookmarkDto.getBoardId();

        Optional<BookmarkEntity> bookmarkOpt = bookmarkRepository.findByMemberEntityIdAndBoardEntityId(memberId, boardId);

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(
            BoardNotFoundException::new);

        if (bookmarkOpt.isPresent()) {
            bookmarkRepository.delete(bookmarkOpt.get());
            return "북마크 취소";
        } else {
            MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(
                MemberNotFoundException::new);
            BookmarkEntity bookmark = new BookmarkEntity(boardEntity, memberEntity);
            bookmarkRepository.save(bookmark);
            return "북마크 완료";
        }
    }

    @Override
    public List<BookmarkEntity> getBookmarks(Long memberId) {
        return bookmarkRepository.findAllByMemberEntityId(memberId);
    }
}