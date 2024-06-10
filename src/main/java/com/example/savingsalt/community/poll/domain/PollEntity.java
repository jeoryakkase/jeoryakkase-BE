package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.global.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "polls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class PollEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;

    private int yesCount;

    private int noCount;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    public boolean isFinished() {
        return LocalDateTime.now().isAfter(endTime);
    }

    public boolean isNotStarted() {
        return LocalDateTime.now().isBefore(startTime);
    }
}
