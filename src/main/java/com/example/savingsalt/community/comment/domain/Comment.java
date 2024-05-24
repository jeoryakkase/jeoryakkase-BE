package com.example.savingsalt.community.comment.domain;

import com.example.savingsalt.community.board.domain.Board;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "comment_id")
    private Long id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
