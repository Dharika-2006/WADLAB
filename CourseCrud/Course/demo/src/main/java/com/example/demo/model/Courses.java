package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Data
@Document(collection = "Courses")
public class Courses {
    @Id
    private String id;
    private String courseid;
    private String coursename;
    private String teacher;
    private Integer credits;
    private Integer seats;      // free seats left
}