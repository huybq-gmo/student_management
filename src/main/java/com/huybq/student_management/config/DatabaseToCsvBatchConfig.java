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
    import org.springframework.batch.item.data.RepositoryItemReader;
    import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
    import org.springframework.batch.item.file.FlatFileItemWriter;
    import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
    import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
    import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.convert.ConversionService;
    import org.springframework.core.convert.converter.Converter;
    import org.springframework.core.convert.support.DefaultConversionService;
    import org.springframework.core.io.FileSystemResource;
    import org.springframework.data.domain.Sort;
    import org.springframework.transaction.PlatformTransactionManager;

    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.HashMap;
    import java.util.Map;

        @Configuration
        @RequiredArgsConstructor
        public class DatabaseToCsvBatchConfig extends BatchConfig {
            private final JobRepository jobRepository;
            private final PlatformTransactionManager transactionManager;
            private final StudentRepository studentRepository;
            private final StudentInfoRepository studentInfoRepository;
            private final UserRepository userRepository;
            private final JobCompletionNotificationListener listener;
            private final StudentInfoItemProcessor studentInfoItemProcessor;




            @Bean
            @StepScope
            public RepositoryItemReader<Student> studentDbReader() {
                Map<String, Sort.Direction> sorts = new HashMap<>();
                sorts.put("id", Sort.Direction.ASC);

                return new RepositoryItemReaderBuilder<Student>()
                        .name("studentDbReader")
                        .repository(studentRepository)
                        .methodName("findAll")
                        .pageSize(100)
                        .sorts(sorts)
                        .build();
            }

            @Bean
            @StepScope
            public RepositoryItemReader<StudentInfo> studentInfoDbReader() {
                Map<String, Sort.Direction> sorts = new HashMap<>();
                sorts.put("id", Sort.Direction.ASC);
                return new RepositoryItemReaderBuilder<StudentInfo>()
                        .name("StudentInfoDbReader")
                        .repository(studentInfoRepository)
                        .methodName("findAll")
                        .pageSize(100)
                        .sorts(sorts)
                        .build();
            }

            @Bean
            @StepScope
            public RepositoryItemReader<User> userDbReader() {
                Map<String, Sort.Direction> sorts = new HashMap<>();
                sorts.put("userId", Sort.Direction.ASC);
                return new RepositoryItemReaderBuilder<User>()
                        .name("UserDbReader")
                        .repository(userRepository)
                        .methodName("findAll")
                        .sorts(sorts)
                        .pageSize(100)
                        .build();
            }

            @Bean
            @StepScope
            public FlatFileItemWriter<Student> studentCsvWriter() {
                return new FlatFileItemWriterBuilder<Student>()
                        .name("studentCsvWriter")
                        .resource(new FileSystemResource("output/students.csv"))
                        .delimited()
                        .delimiter(",")
                        .names("id", "studentCode", "studentName")
                        .headerCallback(writer -> writer.write("id,studentCode,studentName"))
                        .build();
            }

            @Bean
            @StepScope
            public FlatFileItemWriter<StudentInfo> studentInfoCsvWriter() {
                BeanWrapperFieldExtractor<StudentInfo> fieldExtractor = new BeanWrapperFieldExtractor<>();
                fieldExtractor.setNames(new String[]{"id", "studentIdForCsv", "dateOfBirth", "address", "averageScore"});

                DelimitedLineAggregator<StudentInfo> lineAggregator = new DelimitedLineAggregator<>();
                lineAggregator.setDelimiter(",");
                lineAggregator.setFieldExtractor(fieldExtractor);

                return new FlatFileItemWriterBuilder<StudentInfo>()
                        .name("studentInfoCsvWriter")
                        .resource(new FileSystemResource("output/student_infos.csv"))
                        .lineAggregator(lineAggregator)
                        .headerCallback(writer -> writer.write("id,studentId,dateOfBirth,address,averageScore"))
                        .build();
            }


            @Bean
            @StepScope
            public FlatFileItemWriter<User> userCsvWriter() {
                return new FlatFileItemWriterBuilder<User>()
                        .name("userCsvWriter")
                        .resource(new FileSystemResource("output/users.csv"))
                        .delimited()
                        .delimiter(",")
                        .names("userId", "username", "password")
                        .headerCallback(writer -> writer.write("userId,username,password"))
                        .build();
            }

            @Bean
            public StudentItemProcessor studentProcessor() {
                return new StudentItemProcessor();
            }

            @Bean
            public UserItemProcessor userProcessor() {
                return new UserItemProcessor();
            }


            @Bean
            public Step exportStudentsToCsvStep() {
                return createStep(
                        "exportStudentsToCsvStep",
                        jobRepository,
                        transactionManager,
                        studentDbReader(),
                        studentProcessor(),
                        studentCsvWriter(),
                        100);
            }

            @Bean
            public Step exportStudentInfosToCsvStep() {
                return createStep(
                        "exportStudentInfosToCsvStep",
                        jobRepository,
                        transactionManager,
                        studentInfoDbReader(),
                        studentInfoItemProcessor,
                        studentInfoCsvWriter(),
                        100);
            }

            @Bean
            public Step exportUsersToCsvStep() {
                return createStep(
                        "exportUsersToCsvStep",
                        jobRepository,
                        transactionManager,
                        userDbReader(),
                        userProcessor(),
                        userCsvWriter(),
                        100);
            }

            // Job
            @Bean
            public Job exportToCsvJob() {
                return new JobBuilder("exportToCsvJob", jobRepository)
                        .listener(listener)
                        .flow(exportStudentsToCsvStep())
                        .next(exportStudentInfosToCsvStep())
                        .next(exportUsersToCsvStep())
                        .end()
                        .build();
            }


        }