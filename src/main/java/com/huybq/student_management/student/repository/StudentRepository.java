package com.huybq.student_management.student.repository;

import com.huybq.student_management.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    Student findByStudentCode(String studentCode);

}
