package com.example.savingsalt.community.board.repository;

import com.example.savingsalt.community.board.domain.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

}
