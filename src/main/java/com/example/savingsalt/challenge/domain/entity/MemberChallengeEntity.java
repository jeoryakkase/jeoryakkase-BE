package com.example.savingsalt.challenge.domain.entity;

import com.example.savingsalt.member.domain.MemberEntity;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "member_challenges")
@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberChallengeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "certify_date")
    private LocalDateTime successDate;

    @Column(name = "challenge_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    @Column(name = "is_today_certification", nullable = false)
    private Boolean isTodayCertification;

    @Column(name = "auth_count", nullable = false)
    private Integer authCount;

    @Column(name = "success_count", nullable = false)
    private Integer successCount;

    @Column(name = "challenge_comment", length = 50)
    private String challengeComment;

    @Column(name = "total_save_money", nullable = false)
    private Integer totalSaveMoney;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memberChallengeEntity", cascade = CascadeType.ALL)
    private List<CertificationChallengeEntity> certificationChallengeEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private ChallengeEntity challengeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public enum ChallengeStatus {
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
