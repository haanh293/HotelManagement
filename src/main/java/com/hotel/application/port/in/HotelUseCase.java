package com.hotel.application.port.in;

import com.hotel.domain.model.Hotel;
import java.util.List;

public interface HotelUseCase {
    List<Hotel> getAllHotels();
    List<Hotel> searchHotelsByCity(String city);
    Hotel getHotelById(Long id);
    Hotel createHotel(Hotel hotel);
}