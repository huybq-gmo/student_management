package com.huybq.student_management.processor;

import com.huybq.student_management.student.model.Student;
import com.huybq.student_management.student.model.StudentInfo;
import com.huybq.student_management.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StudentInfoItemProcessor implements ItemProcessor<StudentInfo,StudentInfo> {
    private final StudentRepository studentRepository;

    @Override
    public StudentInfo process(StudentInfo studentInfo) throws Exception {
        if (studentInfo.getStudent() == null && studentInfo.getStudentIdForCsv() != null) {
            Student student = studentRepository.findById(studentInfo.getStudentIdForCsv())
                    .orElse(null);
            studentInfo.setStudent(student);
        }
        // Khi ghi từ DB ra CSV, cần lưu ID của Student để sau này có thể map ngược lại
        else if (studentInfo.getStudent() != null && studentInfo.getStudentIdForCsv() == null) {
            studentInfo.setStudentIdForCsv(studentInfo.getStudent().getId());
        }

        return studentInfo;
    }
}
