package com.huybq.student_management.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job {} completed, Time spent: {}ms", jobExecution.getJobInstance().getJobName(), Objects.requireNonNull(jobExecution.getEndTime()).getSecond() - Objects.requireNonNull(jobExecution.getStartTime()).getSecond());
        }
    }
}
