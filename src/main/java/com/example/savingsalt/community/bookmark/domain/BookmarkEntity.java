package com.example.savingsalt.community.bookmark.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bookmarks")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;

    public BookmarkEntity(BoardEntity boardEntity, MemberEntity memberEntity) {
        this.boardEntity = boardEntity;
        this.memberEntity = memberEntity;
    }
}
