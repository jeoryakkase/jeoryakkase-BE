package com.example.savingsalt.community.bookmark.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "bookmarks")
@Getter
@Entity
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "bookmark_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BoardEntity boardEntity;


}
