package com.example.crud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String productName;
    private int quantity;
}
