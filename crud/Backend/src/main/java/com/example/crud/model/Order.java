package com.example.crud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String productName;
    private int quantity;
    private String status;
}

