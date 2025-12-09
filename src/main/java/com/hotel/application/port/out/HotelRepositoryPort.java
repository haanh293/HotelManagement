package com.hotel.application.port.out;

import com.hotel.domain.model.Hotel;
import java.util.List;
import java.util.Optional;

public interface HotelRepositoryPort {
    List<Hotel> findAll();
    List<Hotel> findByCity(String city); // Tìm theo tên thành phố
    Optional<Hotel> findById(Long id);
    Hotel save(Hotel hotel);
}