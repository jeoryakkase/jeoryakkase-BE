package com.example.savingsalt.community.board.service;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.community.board.domain.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.BoardTypeTipReadResDto;
import com.example.savingsalt.community.board.repository.BoardRepository;
import com.example.savingsalt.community.category.domain.CategoryEntity;
import com.example.savingsalt.community.category.repository.CategoryRepository;
import com.example.savingsalt.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;

    private CategoryRepository categoryRepository;

//    private MemberRepository memberRepository;

    @Override
    public BoardTypeTipReadResDto createTipBoard(BoardTypeTipCreateReqDto requestDto) {
        // 인증된 사용자 정보 가져오기(추가예정)

        // 카테고리 정보 가져오기 (예외처리 추가 필요)
        CategoryEntity category = categoryRepository.findById(requestDto.getCategoryId())
            .orElse(null);

        // DTO를 엔터티로 변환 (사용자 정보 코드 추가 후 변경 예정 : new Member() -> member)
        BoardEntity boardEntity = requestDto.toEntity(new Member(), category);

        // 저장된 게시글을 BoardTypeTipReadResDto로 변환하여 반환
        BoardEntity savedBoardEntity = boardRepository.save(boardEntity);

        return convertToBoardTypeTipReadResDto(savedBoardEntity);
    }

    // BoardEntity를 BoardTypeTipReadResDto로 변환
    private BoardTypeTipReadResDto convertToBoardTypeTipReadResDto(BoardEntity boardEntity) {
        return BoardTypeTipReadResDto.builder()
            .id(boardEntity.getId())
            .nickname(boardEntity.getMember().getNickname())
            .title(boardEntity.getTitle())
            .contents(boardEntity.getContents())
            .totalLike(boardEntity.getTotalLike())
            .boardHits(boardEntity.getBoardHits())
            .build();
    }
}

