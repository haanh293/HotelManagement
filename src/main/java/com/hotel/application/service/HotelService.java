package com.hotel.application.service;

import com.hotel.application.port.in.HotelUseCase;
import com.hotel.application.port.out.HotelRepositoryPort;
import com.hotel.domain.model.Hotel;
import com.hotel.domain.model.Review;

import org.springframework.stereotype.Service;
import com.hotel.application.dto.HotelDetailResponse;
import com.hotel.application.port.out.ReviewRepositoryPort;
import java.util.List;

@Service
public class HotelService implements HotelUseCase {

    private final HotelRepositoryPort hotelPort;
    private final ReviewRepositoryPort reviewRepository;
    public HotelService(HotelRepositoryPort hotelPort, ReviewRepositoryPort reviewRepository) {
        this.hotelPort = hotelPort;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelPort.findAll();
    }

    @Override
    public List<Hotel> searchHotelsByCity(String city) {
        return hotelPort.findByCity(city);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelPort.findById(id).orElse(null);
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelPort.save(hotel);
    }
    @Override // Nhớ thêm hàm này vào Interface HotelUseCase nữa nhé!
    public HotelDetailResponse getHotelWithReviews(Long hotelId) {
        // Lấy thông tin khách sạn
        Hotel hotel = hotelPort.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        // Lấy danh sách review của khách sạn đó
        List<Review> reviews = reviewRepository.findByHotelId(hotelId);

        // Gộp lại và trả về
        return new HotelDetailResponse(hotel, reviews);
    }
}