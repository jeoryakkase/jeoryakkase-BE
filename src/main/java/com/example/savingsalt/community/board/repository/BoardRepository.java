package com.example.savingsalt.community.board.repository;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.enums.BoardCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    Page<BoardEntity> findAllByCategoryOrderByCreatedAtDesc(BoardCategory category, Pageable pageable);

    Optional<BoardEntity> findByIdAndCategory(Long id, BoardCategory category);

}
