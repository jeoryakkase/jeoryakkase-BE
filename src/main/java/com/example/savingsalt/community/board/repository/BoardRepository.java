package com.example.savingsalt.community.board.repository;

import com.example.savingsalt.community.board.domain.BoardCategory;
import com.example.savingsalt.community.board.domain.BoardEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findAllByCategoryOrderByCreatedAtDesc(BoardCategory category);

    Optional<BoardEntity> findByIdAndCategory(Long id, BoardCategory category);

}
