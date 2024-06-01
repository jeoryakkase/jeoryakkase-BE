package com.example.savingsalt.community.board.domain.dto;

import com.example.savingsalt.community.board.domain.BoardCategory;
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
    private BoardCategory category;

    public BoardEntity toEntity(MemberEntity memberEntity, BoardCategory category) {
        return BoardEntity.builder()
            .memberEntity(memberEntity)
            .title(this.title)
            .contents(this.contents)
            .category(category)
            .imageUrls(this.imageUrls)
            .build();
    }

}
