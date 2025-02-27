package com.huybq.student_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job")
@RequiredArgsConstructor
public class JobController {
    private final JobLauncher jobLauncher;
    private final Job exportToCsvJob;
    private final Job importFromCsvJob;

    @GetMapping("/start")
    public ResponseEntity<String> startJob(@RequestParam String job) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution;
            if ("exportToCsvJob".equalsIgnoreCase(job)) {
                jobExecution = jobLauncher.run(exportToCsvJob, jobParameters);
            } else if ("importFromCsvJob".equalsIgnoreCase(job)) {
                jobExecution = jobLauncher.run(importFromCsvJob, jobParameters);
            } else {
                return ResponseEntity.badRequest().body("Job không hợp lệ!");
            }

            return ResponseEntity.ok("Job " + job + " đang chạy với ID: " + jobExecution.getId());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi chạy job: " + e.getMessage());
        }
    }
}