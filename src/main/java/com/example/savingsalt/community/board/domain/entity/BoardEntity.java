package com.example.savingsalt.community.board.domain.entity;

import com.example.savingsalt.community.board.domain.dto.BoardTypeTipCreateReqDto;
import com.example.savingsalt.community.board.domain.dto.BoardTypeVoteCreateReqDto;
import com.example.savingsalt.community.board.enums.BoardCategory;
import com.example.savingsalt.community.poll.domain.PollEntity;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.MemberEntity;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "boards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
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

    private String imageUrls;

    @OneToOne(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private PollEntity pollEntity;

    public void updateTipBoard(BoardTypeTipCreateReqDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void updateVoteBoard(BoardTypeVoteCreateReqDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void incrementLikes() {
        this.totalLike++;
    }

    public void decrementLikes() {
        this.totalLike--;
    }
    public void incrementView() {
        this.view++;
    }

}
