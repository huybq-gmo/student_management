package com.huybq.student_management.student.service;

import com.huybq.student_management.student.dto.StudentWithInfoDTO;
import com.huybq.student_management.student.repository.StudentInfoRepository;
import com.huybq.student_management.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentInfoRepository studentInfoRepository;

    public List<StudentWithInfoDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(student -> {
                    var studentInfo = studentInfoRepository.findStudentInfoByStudent_Id(student.getId());
                    return StudentWithInfoDTO.builder()
                            .student(student)
                            .studentInfo(studentInfo)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public StudentWithInfoDTO getStudentById(Integer studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        var studentInfo = studentInfoRepository.findStudentInfoByStudent_Id(studentId);

        return StudentWithInfoDTO.builder()
                .student(student)
                .studentInfo(studentInfo)
                .build();
    }

    @Transactional
    public Integer addStudent(StudentWithInfoDTO studentWithInfoDTO) {
        if (studentWithInfoDTO == null || studentWithInfoDTO.student() == null) {
            throw new IllegalArgumentException("Student data must not be null");
        }

        var student = studentRepository.save(studentWithInfoDTO.student());
        var studentInfo = studentWithInfoDTO.studentInfo();
        if (studentInfo != null) {
            studentInfo.setStudent(student);
            studentInfoRepository.save(studentInfo);
        }
        return student.getId();
    }

    public Integer updateStudent(Integer studentId, StudentWithInfoDTO studentWithInfoDTO) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        var studentInfo = studentInfoRepository.findStudentInfoByStudent_Id(studentId);
        if (studentInfo == null) {
            throw new RuntimeException("Student Info not found for student ID: " + studentId);
        }
        student.setStudentName(studentWithInfoDTO.student().getStudentName());
        student.setStudentCode(studentWithInfoDTO.student().getStudentCode());

        studentInfo.setStudent(student);
        studentInfo.setAddress(studentWithInfoDTO.studentInfo().getAddress());
        studentInfo.setAverageScore(studentWithInfoDTO.studentInfo().getAverageScore());
        studentInfo.setDateOfBirth(studentWithInfoDTO.studentInfo().getDateOfBirth());
        studentRepository.save(student);
        return studentId;
    }

    public Integer deleteStudent(Integer studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        studentRepository.delete(student);
        return studentId;
    }


}
