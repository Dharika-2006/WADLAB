package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.model.Enrollments;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CourseService {
    private final CourseRepository courserepo;
    private final EnrollmentRepository enrollrepo;

    // ---------- Courses collection ----------
    public Courses add(Courses c){
        return courserepo.save(c);
    }
    public List<Courses> getCourses(){
        return courserepo.findAll();
    }
    public Courses getCourse(String courseid){
        return courserepo.findByCourseid(courseid).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
    }

    // courses taught by a teacher
    public List<Courses> getByTeacher(String teacher){
        return courserepo.findByTeacherIgnoreCase(teacher);
    }

    // ---------- Query across BOTH collections ----------

    // courses that students have selected (at least one active enrollment)
    public List<Courses> getSelected(){
        List<Enrollments> enrolled=enrollrepo.findByStatusIgnoreCase("Enrolled");
        List<String> courseids=enrolled.stream().map(Enrollments::getCourseid).distinct().toList();
        return courserepo.findByCourseidIn(courseids);
    }

    // courses selected by one student
    public List<Courses> getByStudent(String studentName){
        List<Enrollments> enrolled=enrollrepo.findByStudentNameIgnoreCaseAndStatusIgnoreCase(studentName,"Enrolled");
        List<String> courseids=enrolled.stream().map(Enrollments::getCourseid).distinct().toList();
        return courserepo.findByCourseidIn(courseids);
    }

    // students enrolled in the courses taught by a teacher
    public List<Enrollments> getStudentsByTeacher(String teacher){
        List<Courses> courses=courserepo.findByTeacherIgnoreCase(teacher);
        List<String> courseids=courses.stream().map(Courses::getCourseid).toList();
        return enrollrepo.findByCourseidInAndStatusIgnoreCase(courseids,"Enrolled");
    }

    // students of one course
    public List<Enrollments> getStudentsByCourse(String courseid){
        return enrollrepo.findByCourseid(courseid);
    }

    // ---------- Enrollments collection ----------

    // enroll only if the course exists, student is not already in it, and a seat is free
    public Enrollments enroll(Enrollments e){
        Courses course=courserepo.findByCourseid(e.getCourseid()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));

        if(enrollrepo.existsByCourseidAndStudentNameAndStatusIgnoreCase(e.getCourseid(),e.getStudentName(),"Enrolled")){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Student already enrolled in this course");
        }
        if(course.getSeats()==null || course.getSeats()<=0){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"No seats available");
        }

        course.setSeats(course.getSeats()-1);
        courserepo.save(course);

        e.setStatus("Enrolled");
        return enrollrepo.save(e);
    }

    // drop an enrollment and give the seat back
    public Enrollments drop(String enrollid){
        Enrollments e=enrollrepo.findByEnrollid(enrollid).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Enrollment not found"));
        if(!"Enrolled".equalsIgnoreCase(e.getStatus())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Already dropped");
        }
        Courses course=courserepo.findByCourseid(e.getCourseid()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));
        course.setSeats((course.getSeats()==null?0:course.getSeats())+1);
        courserepo.save(course);

        e.setStatus("Dropped");
        return enrollrepo.save(e);
    }
}