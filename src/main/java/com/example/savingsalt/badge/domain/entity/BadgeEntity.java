package com.example.savingsalt.badge.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "badges")
@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BadgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "badge_image", nullable = false, length = 3000)
    private String badgeImage;

    @Column(name = "badge_desc", nullable = false)
    private String badgeDesc;

    @Column(name = "badge_type", nullable = false)
    private String badgeType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "badgeEntity", cascade = CascadeType.ALL)
    private List<MemberGoalBadgeEntity> memberGoalBadges = new ArrayList<>();
}
