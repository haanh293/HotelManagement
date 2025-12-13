package com.hotel.application.port.out;

import com.hotel.domain.model.Review;
import java.util.List;

public interface ReviewRepositoryPort {
    Review save(Review review);
    List<Review> findByHotelId(Long hotelId);
	List<Review> findAll();
}