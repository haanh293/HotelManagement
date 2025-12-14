package com.hotel.application.port.in;

import com.hotel.application.dto.HotelDetailResponse;
import com.hotel.domain.model.Hotel;
import java.util.List;

public interface HotelUseCase {
    List<HotelDetailResponse> getAllHotels();
    List<HotelDetailResponse> searchHotelsByCity(String city);
    Hotel getHotelById(Long id);
    Hotel createHotel(Hotel hotel);
    HotelDetailResponse getHotelWithReviews(Long hotelId);
}