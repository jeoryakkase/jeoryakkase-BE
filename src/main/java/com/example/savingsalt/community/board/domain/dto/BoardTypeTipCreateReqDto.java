package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeTipCreateReqDto {

    private String title;
    private String contents;
    private List<String> imageUrls;

    public BoardEntity toEntity(MemberEntity member) {
        return BoardEntity.builder()
            .memberEntity(member)
            .title(title)
            .contents(contents)
            .category(BoardCategory.TIPS)
            .imageUrls(imageUrls)
            .build();
    }

}
