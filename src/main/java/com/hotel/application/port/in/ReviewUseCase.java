package com.hotel.application.port.in;

import com.hotel.domain.model.Review;
import com.hotel.application.dto.ReviewWithHotelResponse;
import java.util.List;

public interface ReviewUseCase {
    Review addReview(Review review);
    List<Review> getReviewsByHotel(Long hotelId);
    List<ReviewWithHotelResponse> getReviewsByGuest(Long guestId);
}