package com.example.savingsalt.community.poll.domain;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.global.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
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
    private BoardEntity board;

    @OneToMany(mappedBy = "pollEntity", cascade = CascadeType.ALL)
    private List<PollChoiceEntity> choices;

    public void addChoices(List<PollChoiceEntity> choices) {
        this.choices = choices;
        for (PollChoiceEntity choice : choices) {
            choice.setPollEntity(this);
        }
    }

}
