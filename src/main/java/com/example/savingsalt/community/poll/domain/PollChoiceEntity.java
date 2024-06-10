package com.example.savingsalt.community.poll.domain;

import jakarta.persistence.Entity;
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

///없앨예정!!!
@Table(name = "pollChoices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class PollChoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity pollEntity;

    private String answer;
    private int count = 0;

    public void setPollEntity(PollEntity pollEntity) {
        this.pollEntity = pollEntity;
    }

    public void incrementCount() {
        this.count++;
    }
}
