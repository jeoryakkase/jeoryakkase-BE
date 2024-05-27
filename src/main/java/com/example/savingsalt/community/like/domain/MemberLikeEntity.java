package com.example.savingsalt.community.like.domain;

import com.example.savingsalt.community.board.domain.BoardEntity;
import com.example.savingsalt.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "memberLikes")
@Getter
@Entity
public class MemberLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "member_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BoardEntity boardEntity;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
