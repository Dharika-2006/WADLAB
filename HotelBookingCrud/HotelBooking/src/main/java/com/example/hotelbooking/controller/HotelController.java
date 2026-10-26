package com.example.hotelbooking.controller;

import com.example.hotelbooking.model.Bookings;
import com.example.hotelbooking.model.Hotels;
import com.example.hotelbooking.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class HotelController {
    private final HotelService hotelservice;

    @PostMapping("/hotels")
    public Hotels add(@RequestBody Hotels h){
        return hotelservice.add(h);
    }

    @GetMapping("/hotels")
    public List<Hotels> getHotels(){
        return hotelservice.getHotels();
    }

    @GetMapping("/hotels/{id}")
    public Hotels getHotel(@PathVariable String id){
        return hotelservice.getHotel(id);
    }

    @GetMapping("/hotels/location/{location}")
    public List<Hotels> getByLocation(@PathVariable String location){
        return hotelservice.getByLocation(location);
    }

    @GetMapping("/hotels/guests")
    public List<Hotels> getByGuest(){
        return hotelservice.getByGuest();
    }

    // NEW: GET /api/hotels/available/Deluxe
    @GetMapping("/hotels/available/{roomType}")
    public List<Hotels> getAvailableByRoomType(@PathVariable String roomType){
        return hotelservice.getAvailableByRoomType(roomType);
    }

    @PostMapping("/bookings")
    public Bookings bookRoom(@RequestBody Bookings b){
        return hotelservice.bookRoom(b);
    }
}