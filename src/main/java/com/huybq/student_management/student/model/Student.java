package com.huybq.student_management.student.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Student",description = "containing infor about student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private Integer id;
    @Column(length = 20,nullable = false)
    private String studentName;
    @Column(length = 10,nullable = false, unique = true)
    private String studentCode;
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private StudentInfo studentInfo;

}
