package com.example.savingsalt.community.board.entity;

import com.example.savingsalt.community.category.entity.Category;
import com.example.savingsalt.global.BaseTimeEntity;
import com.example.savingsalt.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String postTitle;

    private String postContent;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
