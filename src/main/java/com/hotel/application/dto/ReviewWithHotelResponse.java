package com.hotel.application.dto;

import com.hotel.domain.model.Hotel;
import com.hotel.domain.model.Review;

public class ReviewWithHotelResponse {
    private Review review;
    private Hotel hotel;

    public ReviewWithHotelResponse(Review review, Hotel hotel) {
        this.review = review;
        this.hotel = hotel;
    }

    // Getters & Setters
    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
}