package com.example.savingsalt.config.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    @Autowired
    private ResetMemberChallengeAuthenticationTasklet ChallengeTasklet;

    @Autowired
    private UpdateExpiredGoalsTasklet GoalsTasklet;

    @Bean
    public Job resetMemberChallengeJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) throws DuplicateJobException {
        Job job = new JobBuilder("resetMemberChallengeJob", jobRepository)
            .start(resetMemberChallengeStep(jobRepository, transactionManager))
            .build();
        return job;
    }

    @Bean
    public Step resetMemberChallengeStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        Step step = new StepBuilder("resetMemberChallengeStep", jobRepository)
            .tasklet(ChallengeTasklet, transactionManager)
            .build();

        return step;
    }

    @Bean
    public Job updateExpiredGoalsJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) throws DuplicateJobException {
        Job job = new JobBuilder("updateExpiredGoalsJob", jobRepository)
            .start(updateExpiredGoalsStep(jobRepository, transactionManager))
            .build();
        return job;
    }

    @Bean
    public Step updateExpiredGoalsStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        Step step = new StepBuilder("updateExpiredGoalsStep", jobRepository)
            .tasklet(GoalsTasklet, transactionManager)
            .build();

        return step;
    }
}