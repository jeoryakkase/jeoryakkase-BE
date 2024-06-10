package com.example.savingsalt.goal.service;

import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.goal.enums.GoalStatus;
import com.example.savingsalt.goal.repository.GoalRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GoalStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(GoalStatusScheduler.class);
    private final GoalRepository goalRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateExpiredGoals() {
        logger.info("스케줄러 실행 중: 목표 상태 확인 및 업데이트");
        List<GoalEntity> goals = goalRepository.findAll();

        for (GoalEntity goal : goals) {
            if (goal.getGoalStatus() == GoalStatus.PROCEEDING && LocalDate.now().isAfter(goal.getGoalEndDate())) {
                if (goal.getCurrentAmount() < goal.getGoalAmount()) {
                    goal.updateGoalStatus(GoalStatus.FAILURE);
                    goalRepository.save(goal);
                }
            }
        }
    }
}