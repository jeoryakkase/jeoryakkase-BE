package com.example.savingsalt.config.batch;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.goal.repository.GoalRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UpdateExpiredGoalsTasklet implements Tasklet {

    @Autowired
    private GoalRepository goalRepository;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {
        log.info("스케줄러 실행 중: 목표 상태 확인 및 업데이트");

        List<GoalEntity> goals = goalRepository.findAll();

        for (GoalEntity goal : goals) {
            if (goal.getGoalStatus() == GoalStatus.PROCEEDING && LocalDate.now()
                .isAfter(goal.getGoalEndDate())) {
                if (goal.getCurrentAmount() < goal.getGoalAmount()) {
                    goal.updateGoalStatus(GoalStatus.FAILURE);
                    goalRepository.save(goal);
                }
            }
        }
        return RepeatStatus.FINISHED;
    }
}
