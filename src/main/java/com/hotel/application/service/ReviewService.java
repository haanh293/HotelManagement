package com.hotel.application.service;

import com.hotel.application.port.in.ReviewUseCase;
import com.hotel.application.port.out.ReviewRepositoryPort;
import com.hotel.domain.model.Review;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService implements ReviewUseCase {
    private final ReviewRepositoryPort port;

    public ReviewService(ReviewRepositoryPort port) {
        this.port = port;
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
}