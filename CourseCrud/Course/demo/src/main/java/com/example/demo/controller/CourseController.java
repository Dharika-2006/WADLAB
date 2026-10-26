package com.example.demo.controller;

import com.example.demo.model.Courses;
import com.example.demo.model.Enrollments;
import com.example.demo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class CourseController {
    private final CourseService courseservice;

    // ---- courses ----
    @PostMapping("/courses")
    public Courses add(@RequestBody Courses c){
        return courseservice.add(c);
    }

    @GetMapping("/courses")
    public List<Courses> getCourses(){
        return courseservice.getCourses();
    }

    @GetMapping("/courses/{courseid}")
    public Courses getCourse(@PathVariable String courseid){
        return courseservice.getCourse(courseid);
    }

    @GetMapping("/courses/teacher/{teacher}")
    public List<Courses> getByTeacher(@PathVariable String teacher){
        return courseservice.getByTeacher(teacher);
    }

    @GetMapping("/courses/selected")
    public List<Courses> getSelected(){
        return courseservice.getSelected();
    }

    @GetMapping("/courses/student/{studentName}")
    public List<Courses> getByStudent(@PathVariable String studentName){
        return courseservice.getByStudent(studentName);
    }

    // ---- enrollments ----
    @PostMapping("/enrollments")
    public Enrollments enroll(@RequestBody Enrollments e){
        return courseservice.enroll(e);
    }

    @GetMapping("/enrollments/course/{courseid}")
    public List<Enrollments> getStudentsByCourse(@PathVariable String courseid){
        return courseservice.getStudentsByCourse(courseid);
    }

    @PutMapping("/enrollments/{enrollid}/drop")
    public Enrollments drop(@PathVariable String enrollid){
        return courseservice.drop(enrollid);
    }

    // ---- teacher -> students (uses both collections) ----
    @GetMapping("/teachers/{teacher}/students")
    public List<Enrollments> getStudentsByTeacher(@PathVariable String teacher){
        return courseservice.getStudentsByTeacher(teacher);
    }
}