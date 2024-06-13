package com.example.savingsalt.community.board.domain.entity;

import com.example.savingsalt.community.board.domain.dto.MyPageBoardDto;
import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "boards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
@Entity
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String contents;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private int totalLike;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private int view;

    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "boardEntity", cascade = CascadeType.ALL)
    private List<BoardImageEntity> boardImageEntities;

    @OneToOne(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private PollEntity pollEntity;

    public void incrementLikes() {
        this.totalLike++;
    }

    public void decrementLikes() {
        this.totalLike--;
    }

    public void incrementView() {
        this.view++;
    }

    public MyPageBoardDto toMyPageBoardDto() {
        MyPageBoardDto dto = new MyPageBoardDto();
        dto.setId(this.id);
        dto.setMemberId(this.memberEntity.getId());
        dto.setTitle(this.title);
        dto.setContents(this.contents);
        dto.setTotalLike(this.totalLike);
        dto.setView(this.view);
        dto.setCategory(this.category);
        dto.setBoardImageEntities(this.boardImageEntities);
        return dto;
    }

}
