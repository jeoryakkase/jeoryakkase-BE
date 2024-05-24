package com.example.savingsalt.community.like.domain;

import com.example.savingsalt.community.board.domain.Board;
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
public class MemberLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "member_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
