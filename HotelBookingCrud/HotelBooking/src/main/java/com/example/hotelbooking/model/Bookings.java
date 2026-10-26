package com.example.hotelbooking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "Bookings")
public class Bookings {
    @Id
    private String id;

    private String bookingid;
    private String hotelid;
    private String customerName;
    private Integer guest;
    private String status;
}