package com.huybq.student_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
@Configuration
@RequiredArgsConstructor
public class BatchConfig {


    protected <I, O> Step createStep(
            String stepName,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<I> itemReader,
            ItemProcessor<I, O> itemProcessor,
            ItemWriter<O> itemWriter,
            int chunkSize
    ) {
        return new StepBuilder(stepName,jobRepository)
                .<I,O>chunk(100,transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
    protected Job createJob(
            String jobName,
            JobRepository jobRepository,
            Step ...steps
    ){
        SimpleJobBuilder jobBuilder = new JobBuilder(jobName, jobRepository).start(steps[0]);
        for (int i = 1; i < steps.length; i++) {
            jobBuilder = jobBuilder.next(steps[i]);
        }
        return jobBuilder.build();
    }
}
