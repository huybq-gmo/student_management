package com.huybq.student_management.student.dto;

import com.huybq.student_management.student.model.Student;
import com.huybq.student_management.student.model.StudentInfo;
import lombok.Builder;

@Builder
public record StudentWithInfoDTO(Student student, StudentInfo studentInfo) {
}
