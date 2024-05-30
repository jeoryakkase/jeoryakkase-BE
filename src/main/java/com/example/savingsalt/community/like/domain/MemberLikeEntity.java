package com.example.savingsalt.community.like.domain;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "memberLikes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class  MemberLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    public MemberLikeEntity(BoardEntity boardEntity, MemberEntity memberEntity) {
        this.boardEntity = boardEntity;
        this.memberEntity = memberEntity;
    }

}
