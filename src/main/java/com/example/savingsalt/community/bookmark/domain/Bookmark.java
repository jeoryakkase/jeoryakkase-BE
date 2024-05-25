package com.example.savingsalt.community.bookmark.domain;

import com.example.savingsalt.community.board.domain.BoardEntity;
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
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "bookmark_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberEntity;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BoardEntity boardEntity;


}
