package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.category.domain.CategoryEntity;
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
    private Long categoryId;

    // 멤버 엔터티 완성 후 수정 예정 (브랜치 병합 후 이름도 변경 예정 : Member -> MemberEntity)
    public BoardEntity toEntity(MemberEntity memberEntity, CategoryEntity category) {
        return BoardEntity.builder()
            .memberEntity(memberEntity)
            .title(this.title)
            .contents(this.contents)
            .categoryEntity(category)
            .imageUrls(this.imageUrls)
            .build();
    }

}
