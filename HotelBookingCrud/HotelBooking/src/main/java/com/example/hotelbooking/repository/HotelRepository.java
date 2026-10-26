package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Hotels;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends MongoRepository<Hotels, String> {
    Optional<Hotels> findByHotelid(String hotelid);
    List<Hotels> findByHotelidIn(List<String> hotelids);
    List<Hotels> findByLocation(String location);

    // NEW: hotels of a room type that still have free rooms
    List<Hotels> findByRoomTypeIgnoreCaseAndAvailableRoomsGreaterThan(String roomType, Integer rooms);
}