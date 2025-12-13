package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.HotelUseCase;
import com.hotel.domain.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hotel.application.dto.HotelDetailResponse;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelUseCase hotelUseCase;

    public HotelController(HotelUseCase hotelUseCase) {
        this.hotelUseCase = hotelUseCase;
    }

    // --- BỔ SUNG API TẠO KHÁCH SẠN (Để test lưu basicPrice) ---
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelUseCase.createHotel(hotel));
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getHotels(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return ResponseEntity.ok(hotelUseCase.searchHotelsByCity(city));
        }
        return ResponseEntity.ok(hotelUseCase.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelUseCase.getHotelById(id);
        return (hotel != null) ? ResponseEntity.ok(hotel) : ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}/details")
    public ResponseEntity<HotelDetailResponse> getHotelDetails(@PathVariable Long id) {
        return ResponseEntity.ok(hotelUseCase.getHotelWithReviews(id));
    }
}