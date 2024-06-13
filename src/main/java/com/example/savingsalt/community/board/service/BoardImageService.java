package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.dto.BoardImageDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.board.domain.entity.BoardImageEntity;
import com.example.savingsalt.community.board.exception.BoardException.BoardNotFoundException;
import com.example.savingsalt.community.board.repository.BoardImageRepository;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.config.s3.S3Service;
import java.io.IOException;
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

    private final S3Service s3Service;


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

    public List<BoardImageEntity> findAllImageByBoardId(Long boardId) {
        return boardImageRepository.findAllByBoardEntityId(boardId);
    }

    public void deleteBoardImage(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            boardImageRepository.deleteByImageUrl(imageUrl);
            try {
                s3Service.deleteFile(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteAllByBoardEntity(BoardEntity board) {
        boardImageRepository.deleteAllByBoardEntity(board);
    }


    private BoardImageDto convertToBoardImageDto(BoardImageEntity boardImageEntity) {

        return BoardImageDto.builder()
            .imageUrl(boardImageEntity.getImageUrl())
            .build();
    }
}
