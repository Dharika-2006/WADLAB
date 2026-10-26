package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "Enrollments")
public class Enrollments {
    @Id
    private String id;

    private String enrollid;
    private String courseid;
    private String studentName;
    private String status;      // Enrolled or Dropped
}
