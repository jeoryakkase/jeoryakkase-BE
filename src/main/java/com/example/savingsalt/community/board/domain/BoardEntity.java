package com.example.savingsalt.community.board.domain;

import com.example.savingsalt.community.category.domain.CategoryEntity;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "Boards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private int totalLike;

    @Column(nullable = false)
    private int boardHits;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @ElementCollection
    @CollectionTable(name = "board_image_urls", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;


}
