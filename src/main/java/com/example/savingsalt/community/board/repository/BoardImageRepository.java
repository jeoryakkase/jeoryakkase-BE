package com.example.savingsalt.community.board.repository;

import com.example.savingsalt.community.board.domain.entity.BoardImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Long> {

    List<BoardImageEntity> findAllByBoardEntityId(Long boardId);

    void deleteAllByBoardEntityId(Long boardId);

}
