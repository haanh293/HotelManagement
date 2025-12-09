package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.HotelUseCase;
import com.hotel.domain.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelUseCase hotelUseCase;

    public HotelController(HotelUseCase hotelUseCase) {
        this.hotelUseCase = hotelUseCase;
    }

    // API: Lấy tất cả hoặc Tìm theo thành phố (Ví dụ: /api/hotels?city=Đà Lạt)
    @GetMapping
    public ResponseEntity<List<Hotel>> getHotels(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return ResponseEntity.ok(hotelUseCase.searchHotelsByCity(city));
        }
        return ResponseEntity.ok(hotelUseCase.getAllHotels());
    }

    // API: Xem chi tiết 1 khách sạn
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelUseCase.getHotelById(id);
        return (hotel != null) ? ResponseEntity.ok(hotel) : ResponseEntity.notFound().build();
    }
}