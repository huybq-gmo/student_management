package com.huybq.student_management.student;

import com.huybq.student_management.student.controller.StudentController;
import com.huybq.student_management.student.dto.StudentWithInfoDTO;
import com.huybq.student_management.student.model.Student;
import com.huybq.student_management.student.model.StudentInfo;
import com.huybq.student_management.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Mock
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    @Test
    public void getAllStudents() {
        List<StudentWithInfoDTO> expectedStudents = Arrays.asList(
                StudentWithInfoDTO.builder().student(new Student()).studentInfo(new StudentInfo()).build(),
                StudentWithInfoDTO.builder().student(new Student()).studentInfo(new StudentInfo()).build()
        );
        when(service.getAllStudents()).thenReturn(expectedStudents);

        ResponseEntity<List<StudentWithInfoDTO>> response = controller.getAllStudents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudents, response.getBody());
        verify(service).getAllStudents();
    }

    @Test
    public void getAllStudentsNoStudents() {
        when(service.getAllStudents()).thenReturn(Collections.emptyList());

        ResponseEntity<List<StudentWithInfoDTO>> response = controller.getAllStudents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(service).getAllStudents();
    }

    @Test
    public void addStudent() {
        var student = new Student(1, "huybq", "sv002");
        var studentInfo = new StudentInfo(1, "suoi tien", 7.8, LocalDate.of(2002, 1, 15), student, student.getId());
        StudentWithInfoDTO newStudent = StudentWithInfoDTO.builder()
                .student(student)
                .studentInfo(studentInfo)
                .build();
        Integer expectedId = 1;

        when(service.addStudent(any(StudentWithInfoDTO.class))).thenReturn(expectedId);

        ResponseEntity<Integer> response = controller.addStudent(newStudent);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedId, response.getBody());
        verify(service).addStudent(any(StudentWithInfoDTO.class));
    }

    @Test
    public void getStudentById() {
        var student = new Student(1, "huybq", "sv002");
        var studentInfo = new StudentInfo(1, "suoi tien", 7.8, LocalDate.of(2002, 1, 15), student, student.getId());
        StudentWithInfoDTO expectedStudent = StudentWithInfoDTO.builder()
                .student(student)
                .studentInfo(studentInfo)
                .build();
        Integer studentId = 1;

        when(service.getStudentById(studentId)).thenReturn(expectedStudent);
        ResponseEntity<StudentWithInfoDTO> response = controller.getStudentById(studentId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudent, response.getBody());
        verify(service).getStudentById(studentId);
    }

    @Test
    public void updateStudent() {
        var student = new Student(1, "huybq", "sv002");
        var studentInfo = new StudentInfo(1, "tphcm", 7.8, LocalDate.of(2002, 5, 20), student, student.getId());
        Integer studentId = 1;
        StudentWithInfoDTO updatedStudent = StudentWithInfoDTO.builder()
                .student(student)
                .studentInfo(studentInfo)
                .build();

        when(service.updateStudent(eq(studentId), any(StudentWithInfoDTO.class))).thenReturn(studentId);
        ResponseEntity<Integer> response = controller.updateStudent(studentId, updatedStudent);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(studentId, response.getBody());
        verify(service).updateStudent(eq(studentId), any(StudentWithInfoDTO.class));
    }

    @Test
    public void deleteStudent() {
        Integer studentId = 1;
        when(service.deleteStudent(studentId)).thenReturn(studentId);

        ResponseEntity<Integer> response = controller.deleteStudent(studentId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(studentId, response.getBody());
        verify(service).deleteStudent(studentId);
    }

    @Test
    public void getStudentByIdNonExistent() {
        Integer nonExistentId = 999;
        when(service.getStudentById(nonExistentId)).thenThrow(new RuntimeException("Student not found with ID: " + nonExistentId));

        assertThrows(RuntimeException.class, () -> controller.getStudentById(nonExistentId));
        verify(service).getStudentById(nonExistentId);
    }

    @Test
    public void addStudentWithNullStudent() {
        doThrow(new IllegalArgumentException("Student data must not be null"))
                .when(service).addStudent(null);

        assertThrows(IllegalArgumentException.class, () -> controller.addStudent(null));
        verify(service).addStudent(null);
    }
}
