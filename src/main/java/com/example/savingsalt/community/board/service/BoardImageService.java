package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.dto.BoardImageDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.domain.entity.BoardImageEntity;
import com.example.savingsalt.community.board.exception.BoardException.BoardNotFoundException;
import com.example.savingsalt.community.board.repository.BoardImageRepository;
import com.example.savingsalt.community.board.repository.BoardRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;

    private final BoardRepository boardRepository;


    public List<BoardImageDto> createBoardImage(List<String> imageUrls, Long boardId) {

        List<BoardImageDto> boardImageDtos = new ArrayList<>();

        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardId);
        BoardEntity boardEntity = optionalBoardEntity.orElseThrow(
            () -> new BoardNotFoundException());

        for (String imageUrl : imageUrls) {
            BoardImageEntity boardImageEntity = BoardImageEntity.builder()
                .imageUrl(imageUrl)
                .boardEntity(boardEntity)
                .build();

            boardImageRepository.save(boardImageEntity);

            boardImageDtos.add(convertToBoardImageDto(boardImageEntity));
        }

        return boardImageDtos;
    }


    private BoardImageDto convertToBoardImageDto(BoardImageEntity boardImageEntity) {

        return BoardImageDto.builder()
            .imageUrl(boardImageEntity.getImageUrl())
            .build();
    }
}
