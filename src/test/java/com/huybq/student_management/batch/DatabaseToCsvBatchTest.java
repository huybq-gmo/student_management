package com.huybq.student_management.batch;

import com.huybq.student_management.student.model.Student;
import com.huybq.student_management.student.model.StudentInfo;
import com.huybq.student_management.student.repository.StudentInfoRepository;
import com.huybq.student_management.student.repository.StudentRepository;
import com.huybq.student_management.user.model.User;
import com.huybq.student_management.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringBatchTest
class DatabaseToCsvBatchTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job exportToCsvJob;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentInfoRepository studentInfoRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportStudentsToCsv() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1, "S001", "huybq"),
                new Student(2, "S002", "datth")
        );
        when(studentRepository.findAll()).thenReturn(students);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(exportToCsvJob, jobParameters);

        assertThat(jobExecution.getStatus().isUnsuccessful()).isFalse();
    }

    @Test
    void testExportStudentInfosToCsv() throws Exception {
        var student1 = new Student(1, "S001", "huybq");
        var student2 = new Student(2, "S002", "datth");

        List<StudentInfo> studentInfos = Arrays.asList(
                new StudentInfo(1, "hcm", 8.5, LocalDate.of(2002, 12, 1), student1, 1),
                new StudentInfo(2, "hn", 90.0, LocalDate.parse("1999-05-15"), student2, 2)
        );
        when(studentInfoRepository.findAll()).thenReturn(studentInfos);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(exportToCsvJob, jobParameters);

        assertThat(jobExecution.getStatus().isUnsuccessful()).isFalse();
    }

    @Test
    void testExportUsersToCsv() throws Exception {
        List<User> users = Arrays.asList(
                new User(1, "user1", "password1"),
                new User(2, "user2", "password2")
        );
        when(userRepository.findAll()).thenReturn(users);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(exportToCsvJob, jobParameters);

        assertThat(jobExecution.getStatus().isUnsuccessful()).isFalse();
    }
}
