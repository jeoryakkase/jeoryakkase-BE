package com.example.savingsalt.config.batch;

import com.example.savingsalt.challenge.service.MemberChallengeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig extends DefaultBatchConfiguration {

    @Autowired
    private final MemberChallengeServiceImpl memberChallengeService;
    @Bean
    public Job certificationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws DuplicateJobException {
        Job job = new JobBuilder("certificationJob",jobRepository)
            .start(certificationStep(jobRepository,transactionManager))
            .build();
        return job;
    }

    public Step certificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Step step = new StepBuilder("certificationStep", jobRepository)
            .tasklet(certificationTasklet(), transactionManager)
            .build();

        return step;
    }

    public Tasklet certificationTasklet() {
        return (((contribution, chunkContext) -> {
            log.info("***** 챌린지 인증 초기화 *****");
            memberChallengeService.resetDailyMemberChallengeAuthentication();
            return RepeatStatus.FINISHED;
        }));
    }

}
