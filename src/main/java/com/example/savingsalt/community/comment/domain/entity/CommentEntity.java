package com.example.savingsalt.community.comment.domain.entity;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String nickname;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public CommentEntity(CommentReqDto requestDto, BoardEntity saveBoard, MemberEntity member) {
        this.nickname = member.getNickname();
        this.content = requestDto.getContent();
        this.boardEntity = saveBoard;
        this.memberEntity = member;
    }

    public void update(CommentReqDto requestDto) {
        this.content = requestDto.getContent();
    }

}
