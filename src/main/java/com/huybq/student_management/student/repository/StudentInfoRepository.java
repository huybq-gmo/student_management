package com.huybq.student_management.student.repository;


import com.huybq.student_management.student.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo,Integer> {
    StudentInfo findStudentInfoByStudent_Id(Integer studentId);

    Integer deleteStudentInfoByStudent_Id(Integer studentId);

}

