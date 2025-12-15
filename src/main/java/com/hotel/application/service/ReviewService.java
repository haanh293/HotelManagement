package com.hotel.application.service;

import com.hotel.application.dto.ReviewWithHotelResponse;
import com.hotel.application.port.in.ReviewUseCase;
import com.hotel.application.port.out.HotelRepositoryPort;
import com.hotel.application.port.out.ReviewRepositoryPort;
import com.hotel.domain.model.Hotel;
import com.hotel.domain.model.Review;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class ReviewService implements ReviewUseCase {
    private final ReviewRepositoryPort port;
    private final HotelRepositoryPort hotelPort;

    public ReviewService(ReviewRepositoryPort port, HotelRepositoryPort hotelPort) {
        this.port = port;
        this.hotelPort = hotelPort;
    }

    @Override
    public Review addReview(Review review) {
        review.setReviewDate(LocalDate.now()); 
        return port.save(review);
    }

    @Override
    public List<Review> getReviewsByHotel(Long hotelId) {
        return port.findByHotelId(hotelId);
    }
    @Override
    public List<ReviewWithHotelResponse> getReviewsByGuest(Long guestId) {
        // Lấy danh sách review của khách
        List<Review> reviews = port.findByGuestId(guestId);
        
        List<ReviewWithHotelResponse> responseList = new ArrayList<>();

        // B2: Duyệt qua từng review để lấy thông tin Hotel
        for (Review review : reviews) {
            // Tìm hotel theo ID (orElse(null) phòng trường hợp hotel bị xóa)
            Hotel hotel = hotelPort.findById(review.getHotelId()).orElse(null);
            
            // B3: Gộp vào DTO
            responseList.add(new ReviewWithHotelResponse(review, hotel));
        }

        return responseList;
    }
}