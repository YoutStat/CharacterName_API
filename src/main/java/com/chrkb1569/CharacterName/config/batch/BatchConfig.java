package com.chrkb1569.CharacterName.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final Tasklet tasklet;
    private final CustomJobListener jobListener;

    @Bean
    public Job CharacterName(JobRepository jobRepository, Step characterNameStep) {
        return new JobBuilder("characterNameJob", jobRepository)
                .start(characterNameStep)
                .listener(jobListener)
                .build();
    }

    @Bean
    public Step getStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("characterNameStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}