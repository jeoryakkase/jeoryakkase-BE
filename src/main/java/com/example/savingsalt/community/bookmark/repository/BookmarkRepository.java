package com.example.savingsalt.community.bookmark.repository;

import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByMemberEntityIdAndBoardEntityId(Long memberId, Long boardId);
    List<BookmarkEntity> findAllByMemberEntityId(Long memberId);
}
