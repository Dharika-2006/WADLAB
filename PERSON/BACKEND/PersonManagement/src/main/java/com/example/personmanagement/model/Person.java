package com.example.personmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "Name")
public class Person {
    @Id
    private String id;
    private String name;
}
