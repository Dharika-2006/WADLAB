package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Bookings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Bookings, String> {
    List<Bookings> findByHotelid(String hotelid);
    List<Bookings> findByStatusIgnoreCase(String status);
    List<Bookings> findByStatusIgnoreCaseAndGuestGreaterThan(String status, Integer guest); // fixed: Guest, not Guests
}