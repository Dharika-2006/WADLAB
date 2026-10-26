package com.example.demo.repository;


import com.example.demo.model.Courses;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Courses, String> {
    Optional<Courses> findByCourseid(String courseid);
    List<Courses> findByCourseidIn(List<String> courseids);
    List<Courses> findByTeacherIgnoreCase(String teacher);
}