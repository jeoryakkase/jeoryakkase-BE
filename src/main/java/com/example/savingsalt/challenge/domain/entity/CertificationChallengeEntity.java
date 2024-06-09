package com.example.savingsalt.challenge.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "certification_challenges")
@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CertificationChallengeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certification_date", nullable = false)
    private LocalDateTime certificationDate;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "save_money", nullable = false)
    private Integer saveMoney;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_challenge_id")
    private MemberChallengeEntity memberChallengeEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "certificationChallenge", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CertificationChallengeImageEntity> certificationChallengeImageEntities;

}
