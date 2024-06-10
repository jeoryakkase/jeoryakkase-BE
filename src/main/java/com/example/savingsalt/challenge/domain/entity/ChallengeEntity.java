package com.example.savingsalt.challenge.domain.entity;

import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.global.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "challenges")
@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "challenge_title", nullable = false, unique = true, length = 50)
    private String challengeTitle;

    @Column(name = "challenge_desc", nullable = false)
    private String challengeDesc;

    @Column(name = "challenge_goal")
    private int challengeGoal;

    @Column(name = "challenge_count")
    private int challengeCount;

    @Column(name = "challenge_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeType challengeType;

    @Column(name = "challenge_term", nullable = false, length = 20)
    private String challengeTerm;

    @Column(name = "challenge_difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeDifficulty challengeDifficulty;

    @Column(name = "auth_content", nullable = false, length = 50)
    private String authContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private BadgeEntity badgeEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "challengeEntity", cascade = CascadeType.ALL)
    private List<MemberChallengeEntity> memberChallengeEntities;

    public enum ChallengeDifficulty {
        UNSET,
        EASY,
        NORMAL,
        HARD
    }

    public enum ChallengeType {
        COUNT,
        GOAL
    }

}
