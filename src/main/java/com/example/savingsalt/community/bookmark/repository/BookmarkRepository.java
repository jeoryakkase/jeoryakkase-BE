package com.example.savingsalt.community.bookmark.repository;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

    BookmarkEntity findByMemberEntityAndBoardEntity(MemberEntity memberEntity, BoardEntity boardEntity);

    List<BookmarkEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
