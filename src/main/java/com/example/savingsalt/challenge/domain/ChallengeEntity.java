package com.example.savingsalt.challenge.domain;

import com.example.savingsalt.badge.domain.BadgeEntity;
import com.example.savingsalt.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @Column(name = "challenge_title", nullable = false, length = 50)
    private String challengeTitle;

    @Column(name = "challenge_desc", nullable = false)
    private String challengeDesc;

    @Column(name = "challenge_goal")
    private int challengeGoal;

    @Column(name = "challenge_count")
    private int challengeCount;

    @Column(name = "challenge_type", nullable = false, length = 20)
    private String challengeType;

    @Column(name = "challenge_term", nullable = false, length = 20)
    private String challengeTerm;

    @Column(name = "challenge_difficulty", nullable = false)
    private ChallengeDifficulty challengeDifficulty;

    public enum ChallengeDifficulty {
        UNSET,
        EASY,
        NORMAL,
        HARD
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badges_id")
    private BadgeEntity badgeEntity;
}
