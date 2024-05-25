package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.category.domain.CategoryEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeTipCreateReqDto {

    private String boardtitle;
    private String boardcontents;
    private List<String> imageUrls;
    private Long categoryId;

    public BoardEntity toEntity(MemberEntity member, CategoryEntity category) {
        return new BoardEntity(null, member, this.boardtitle, this.boardcontents, category);
    }

}
