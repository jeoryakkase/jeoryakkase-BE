package com.example.savingsalt.goal.domain.entity;

import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.member.domain.MemberEntity;
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
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "goals")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "goal_title")
    private String goalTitle;

    @Column(name = "goal_amount")
    private Long goalAmount;

    @Column(name = "goal_image")
    private String goalImage;

    @Column(name = "goal_start_date")
    private LocalDate goalStartDate;

    @Column(name = "goal_end_date")
    private LocalDate goalEndDate;

    @Column(name = "current_amount")
    private Long currentAmount;

    @Enumerated(EnumType.STRING) // Enum을 String 형태로 저장
    @Column(name = "goal_status")
    private GoalStatus goalStatus;

    // 인증 금액을 추가하는 메소드
    public void updateGoalStatus(GoalStatus goalStatus) {
        this.goalStatus = goalStatus;
    }

    // 인증 금액을 차감하는 메소드
    public void subtractCertificationMoney(Long amount) {
        this.currentAmount -= amount;
    }

    // currentAmount 에 돈을 누적하는 메소드
    public void addCertificationMoney(Long certificationMoney) {
        if (this.currentAmount == null) {
            this.currentAmount = 0L;
        }
        this.currentAmount += certificationMoney;
    }
}
