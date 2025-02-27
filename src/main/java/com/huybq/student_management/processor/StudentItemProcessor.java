package com.huybq.student_management.processor;

import com.huybq.student_management.student.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
public class StudentItemProcessor implements ItemProcessor<Student, Student> {
    @Override
    public Student process(final Student student) throws Exception {
        log.info("Process student: {}", student);
        return student;
    }
}
