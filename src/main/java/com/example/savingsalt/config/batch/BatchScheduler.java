package com.example.savingsalt.config.batch;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        jobProcessor.setJobRegistry(jobRegistry);
        return jobProcessor;
    }

    // 모든 회원 챌린지 일일 인증 및 회원 목표 상태 초기화(오전 12시마다)
    @Scheduled(cron = "0 0 0 * * *") // 매일 00시 정각
    public void runJobs() {
        String challengeTime = LocalDateTime.now().toString();
        String goalTime = LocalDateTime.now().toString();

        try {
            Job challengeJob = jobRegistry.getJob("resetMemberChallengeJob"); // job 이름
            JobParametersBuilder jobParam = new JobParametersBuilder().addString(
                "challengeResetTime", challengeTime);
            jobLauncher.run(challengeJob, jobParam.toJobParameters());

            Job goalJob = jobRegistry.getJob("resetMemberChallengeJob"); // job 이름
            JobParametersBuilder goalJobParam = new JobParametersBuilder().addString(
                "goalResetTime", goalTime);
            jobLauncher.run(goalJob, goalJobParam.toJobParameters());

        } catch (NoSuchJobException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException |
                 JobExecutionAlreadyRunningException |
                 JobParametersInvalidException |
                 JobRestartException e
        ) {
            throw new RuntimeException(e);
        }
    }
}
