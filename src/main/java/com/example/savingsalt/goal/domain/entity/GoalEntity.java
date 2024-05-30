package com.example.savingsalt.goal.domain.entity;

import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "goal_amount")
    private Long goalAmount;

    @Column(name = "goal_desc")
    private String goalDesc;

    @Column(name = "goal_start_date")
    private LocalDateTime goalStartDate;

    @Column(name = "goal_end_date")
    private LocalDateTime goalEndDate;

    @Column(name = "current_amount")
    private Long currentAmount;

    @Column(name = "goal_status")
    private String goalStatus;
}
