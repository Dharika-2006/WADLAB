package com.example.employeeleavemanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    private String empid;
    private String name;
    private String dept;
    private int leavebal;

}
