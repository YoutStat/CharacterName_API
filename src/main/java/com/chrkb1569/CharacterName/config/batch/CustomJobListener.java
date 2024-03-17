package com.chrkb1569.CharacterName.config.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomJobListener implements JobExecutionListener {
    @Value("${api.request.name.initialPage}")
    private long PAGE_VALUE;

    @Value("${api.request.name.pageKey}")
    private String PAGE_KEY;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().putLong(PAGE_KEY, PAGE_VALUE);
    }
}