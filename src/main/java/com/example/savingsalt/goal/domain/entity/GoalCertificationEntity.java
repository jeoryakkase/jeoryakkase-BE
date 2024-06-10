package com.example.savingsalt.goal.domain.entity;

import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "goals_certification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GoalCertificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private GoalEntity goalEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "certification_money")
    private Long certificationMoney;

    @Column(name = "certification_content")
    private String certificationContent;

    @Column(name = "certification_image_url")
    private String certificationImageUrl;

    @Column(name = "certification_date")
    private LocalDate certificationDate;
}
