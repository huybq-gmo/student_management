package com.huybq.student_management.config;

import com.huybq.student_management.listener.JobCompletionNotificationListener;
import com.huybq.student_management.processor.StudentInfoItemProcessor;
import com.huybq.student_management.processor.StudentItemProcessor;
import com.huybq.student_management.processor.UserItemProcessor;
import com.huybq.student_management.student.model.Student;
import com.huybq.student_management.student.model.StudentInfo;
import com.huybq.student_management.student.repository.StudentInfoRepository;
import com.huybq.student_management.student.repository.StudentRepository;
import com.huybq.student_management.user.model.User;
import com.huybq.student_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor

public class CsvToDatabaseBatchConfig extends BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StudentRepository studentRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final UserRepository userRepository;
    private final JobCompletionNotificationListener listener;
    private final StudentInfoItemProcessor studentInfoItemProcessor;


@Bean
public ConversionService testConversionService() {
    DefaultConversionService conversionService = new DefaultConversionService();
    conversionService.addConverter(new Converter<String, LocalDate>() {
        @Override
        public LocalDate convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }
            return LocalDate.parse(source, DateTimeFormatter.ISO_DATE);
        }
    });
    return conversionService;
}
    @Bean
    @StepScope
    public FlatFileItemReader<Student> studentCsvReader() {
        return new FlatFileItemReaderBuilder<Student>()
                .name("studentCsvReader")
                .resource(new FileSystemResource("output/students.csv"))
                .delimited()
                .delimiter(",")
                .names("id", "studentCode", "studentName")
                .linesToSkip(1) // Skip header
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Student>() {{
                    setTargetType(Student.class);
                }})
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<StudentInfo> studentInfoCsvReader() {
        BeanWrapperFieldSetMapper<StudentInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentInfo.class);
        fieldSetMapper.setConversionService(testConversionService());

        return new FlatFileItemReaderBuilder<StudentInfo>()
                .name("studentInfoCsvReader")
                .resource(new FileSystemResource("output/student_infos.csv"))
                .delimited()
                .delimiter(",")
                .names("id", "studentIdForCsv", "dateOfBirth", "address", "averageScore")
                .linesToSkip(1) // Skip header
                .fieldSetMapper(fieldSetMapper)
                .build();
    }


    @Bean
    @StepScope
    public FlatFileItemReader<User> userCsvReader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userCsvReader")
                .resource(new FileSystemResource("output/users.csv"))
                .delimited()
                .delimiter(",")
                .names("userId", "username", "password")
                .linesToSkip(1) // Skip header
                .fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                    setTargetType(User.class);
                }})
                .build();
    }


    @Bean
    public RepositoryItemWriter<Student> studentDatabaseWriter() {
        return new RepositoryItemWriterBuilder<Student>()
                .repository(studentRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public RepositoryItemWriter<StudentInfo> studentInfoDatabaseWriter() {
        return new RepositoryItemWriterBuilder<StudentInfo>()
                .repository(studentInfoRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public RepositoryItemWriter<User> userDatabaseWriter() {
        return new RepositoryItemWriterBuilder<User>()
                .repository(userRepository)
                .methodName("save")
                .build();
    }
    @Bean
    public StudentItemProcessor studentCsvProcessor() {
        return new StudentItemProcessor();
    }

    @Bean
    public UserItemProcessor userCsvProcessor() {
        return new UserItemProcessor();
    }



    @Bean
    public Step importStudentsFromCsvStep() {
        return createStep(
                "importStudentsFromCsvStep",
                jobRepository,
                transactionManager,
                studentCsvReader(),
                studentCsvProcessor(),
                studentDatabaseWriter(),
                100);
    }

    @Bean
    public Step importStudentInfosFromCsvStep() {
        return createStep(
                "importStudentInfosFromCsvStep",
                jobRepository,
                transactionManager,
                studentInfoCsvReader(),
                studentInfoItemProcessor,
                studentInfoDatabaseWriter(),
                100);
    }

    @Bean
    public Step importUsersFromCsvStep() {
        return createStep(
                "importUsersFromCsvStep",
                jobRepository,
                transactionManager,
                userCsvReader(),
                userCsvProcessor(),
                userDatabaseWriter(),
                100);
    }

    // Job
    @Bean
    public Job importFromCsvJob() {
        return new JobBuilder("importFromCsvJob", jobRepository)
                .listener(listener)
                .flow(importStudentsFromCsvStep())
                .next(importStudentInfosFromCsvStep())
                .next(importUsersFromCsvStep())
                .end()
                .build();
    }





}
