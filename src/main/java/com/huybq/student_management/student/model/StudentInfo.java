package com.huybq.student_management.student.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "student_info")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Student info", description = "containing infor detail about student")
public class StudentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "info_id")
    private Integer id;
    private String address;
    private double averageScore;
    private LocalDate dateOfBirth;
    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;
    @Column(name = "student_id_for_csv")
    @JsonIgnore
    private Integer studentIdForCsv;


}
