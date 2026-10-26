package com.example.hotelbooking.service;

import com.example.hotelbooking.model.Bookings;
import com.example.hotelbooking.model.Hotels;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class HotelService {
    private final HotelRepository hotelrepo;
    private final BookingRepository bookingrepo;

    public Hotels add(Hotels h){
        return hotelrepo.save(h);
    }
    public List<Hotels> getHotels(){
        return hotelrepo.findAll();
    }
    public Hotels getHotel(String id){
        return hotelrepo.findByHotelid(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Hotel not found"));
    }
    public List<Hotels> getByLocation(String location){
        return hotelrepo.findByLocation(location);
    }
    public List<Hotels> getByGuest(){
        List<Bookings> bookings=bookingrepo.findByStatusIgnoreCaseAndGuestGreaterThan("Confirmed",3);
        List<String> hotelids=bookings.stream().map(Bookings::getHotelid).distinct().toList();
        return hotelrepo.findByHotelidIn(hotelids);
    }

    // CHANGED: book only if a room is free, then reduce the free room count
    public Bookings bookRoom(Bookings b){
        Hotels hotel = hotelrepo.findByHotelid(b.getHotelid()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Hotel not found"));

        if(hotel.getAvailableRooms()==null || hotel.getAvailableRooms()<=0){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"No rooms available");
        }

        hotel.setAvailableRooms(hotel.getAvailableRooms()-1);
        hotelrepo.save(hotel);

        b.setStatus("Confirmed");
        return bookingrepo.save(b);
    }

    // NEW: available hotels/rooms by room type
    public List<Hotels> getAvailableByRoomType(String roomType){
        return hotelrepo.findByRoomTypeIgnoreCaseAndAvailableRoomsGreaterThan(roomType,0);
    }
}