package com.example.savingsalt.community.comment.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Getter
@Entity
@NoArgsConstructor
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public CommentEntity(CommentReqDto requestDto, BoardEntity saveBoard, MemberEntity member) {
        this.nickname = member.getNickname();
        this.comment = requestDto.getComment();
        this.boardEntity = saveBoard;
        this.memberEntity = member;
    }

    public void update(CommentReqDto requestDto) {
        this.comment = requestDto.getComment();
    }

}