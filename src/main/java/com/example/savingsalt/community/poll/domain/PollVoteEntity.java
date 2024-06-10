package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.poll.enums.PollVoteChoice;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "poll_votes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class PollVoteEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity pollEntity;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PollVoteChoice pollVoteChoice;
}
