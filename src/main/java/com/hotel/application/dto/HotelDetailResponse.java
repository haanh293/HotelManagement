package com.hotel.application.dto;

import com.hotel.domain.model.Hotel;
import com.hotel.domain.model.Review;
import java.util.List;

public class HotelDetailResponse {
    private Hotel hotel;
    private List<Review> reviews;
    private double averageRating;

    public HotelDetailResponse(Hotel hotel, List<Review> reviews) {
        this.hotel = hotel;
        this.reviews = reviews;
        this.averageRating = calculateAverage(reviews);
    }

    private double calculateAverage(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    // Getters
    public Hotel getHotel() { return hotel; }
    public List<Review> getReviews() { return reviews; }
    public double getAverageRating() { return averageRating; }
}