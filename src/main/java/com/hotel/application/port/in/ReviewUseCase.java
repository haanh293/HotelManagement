package com.hotel.application.port.in;

import com.hotel.domain.model.Review;
import java.util.List;

public interface ReviewUseCase {
    Review addReview(Review review);
    List<Review> getReviewsByRoom(Long roomId);
}