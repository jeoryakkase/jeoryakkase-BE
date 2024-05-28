package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "comments")
@Getter
@Entity
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BoardEntity boardEntity;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

}
