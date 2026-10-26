package com.example.hotelbooking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Data
@Document(collection = "Hotels")
public class Hotels {
    @Id
    private String id;
    private String hotelid;
    private String hotelname;
    private String location;
    private String roomType;
    private Double price;
    private Integer availableRooms;   // NEW: number of free rooms
}