package com.huybq.student_management.student.controller;

import com.huybq.student_management.student.dto.StudentWithInfoDTO;
import com.huybq.student_management.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Origin"})
@Tag(name = "Student", description = "managing student")
public class StudentController {
    private final StudentService service;

    @GetMapping
    @Operation(summary = "get list student", description = "get all student")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<List<StudentWithInfoDTO>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @Operation(summary = "get student", description = "get student by id")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public ResponseEntity<StudentWithInfoDTO> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    @PostMapping
    @Operation(summary = "add student", description = "add student into list")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Integer> addStudent(@RequestBody StudentWithInfoDTO studentWithInfoDTO) {
        Integer studentId = service.addStudent(studentWithInfoDTO);
        return ResponseEntity.ok(studentId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update student", description = "update student in list")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Integer> updateStudent(@PathVariable Integer id, @RequestBody StudentWithInfoDTO studentWithInfoDTO) {
        Integer updatedId = service.updateStudent(id, studentWithInfoDTO);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "remove student", description = "remove student from the list")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Integer> deleteStudent(@PathVariable Integer id) {
        Integer deletedId = service.deleteStudent(id);
        return ResponseEntity.ok(deletedId);
    }
}
