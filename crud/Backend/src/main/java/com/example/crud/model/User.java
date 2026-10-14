package com.example.crud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
}

