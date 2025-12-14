package com.hotel.application.service;

import com.hotel.application.port.in.HotelUseCase;
import com.hotel.application.port.out.HotelRepositoryPort;
import com.hotel.domain.model.Hotel;
import com.hotel.domain.model.Review;

import org.springframework.stereotype.Service;
import com.hotel.application.dto.HotelDetailResponse;
import com.hotel.application.port.out.ReviewRepositoryPort;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelService implements HotelUseCase {

    private final HotelRepositoryPort hotelPort;
    private final ReviewRepositoryPort reviewRepository;
    public HotelService(HotelRepositoryPort hotelPort, ReviewRepositoryPort reviewRepository) {
        this.hotelPort = hotelPort;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<HotelDetailResponse> getAllHotels() {
        // 1. Lấy danh sách tất cả Hotel
        List<Hotel> hotels = hotelPort.findAll();

        // 2. Lấy danh sách tất cả Review
        List<Review> allReviews = reviewRepository.findAll();

        // 3. Gom nhóm Review theo hotelId bằng Map
        Map<Long, List<Review>> reviewsByHotelId = allReviews.stream()
                .collect(Collectors.groupingBy(Review::getHotelId));

        // 4. Map từng Hotel sang HotelDetailResponse
        return hotels.stream()
                .map(hotel -> {
                    List<Review> reviews = reviewsByHotelId.getOrDefault(hotel.getId(), new ArrayList<>());
                    return new HotelDetailResponse(hotel, reviews);
                })
                .collect(Collectors.toList());
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
    @Override 
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