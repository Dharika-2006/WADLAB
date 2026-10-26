package com.example.demo.repository;

import com.example.demo.model.Enrollments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollments, String> {
    Optional<Enrollments> findByEnrollid(String enrollid);
    List<Enrollments> findByCourseid(String courseid);
    List<Enrollments> findByStatusIgnoreCase(String status);
    List<Enrollments> findByStudentNameIgnoreCaseAndStatusIgnoreCase(String studentName, String status);
    List<Enrollments> findByCourseidInAndStatusIgnoreCase(List<String> courseids, String status);
    boolean existsByCourseidAndStudentNameAndStatusIgnoreCase(String courseid, String studentName, String status);
}
