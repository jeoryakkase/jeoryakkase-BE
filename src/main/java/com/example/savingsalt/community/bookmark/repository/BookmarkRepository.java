package com.example.savingsalt.community.bookmark.repository;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    boolean existsByBoardEntityAndMemberEntity(BoardEntity boardEntity, MemberEntity memberEntity);
    void deleteByBoardEntityAndMemberEntity(BoardEntity boardEntity, MemberEntity memberEntity);
    long countByBoardEntity(BoardEntity boardEntity);
    List<BookmarkEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
