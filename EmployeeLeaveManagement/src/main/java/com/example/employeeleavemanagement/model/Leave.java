package com.example.employeeleavemanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "leave")

public class Leave {
    @Id
    private String id;

    private String empid;
    private String leaveType;
    private int requestedDays;
    private String status;
}
