package com.hotel.domain.model;

import java.time.LocalDate;

public class Review {
    private Long id;
    private Long guestId; // Ai đánh giá
    private Long hotelId; // <-- ĐỔI TỪ roomId SANG hotelId (Đánh giá khách sạn nào)
    private Integer rating; 
    private String comment; 
    private LocalDate reviewDate; 

    public Review() {}

    // Cập nhật Constructor
    public Review(Long id, Long guestId, Long hotelId, Integer rating, String comment, LocalDate reviewDate) {
        this.id = id;
        this.guestId = guestId;
        this.hotelId = hotelId; // <-- Cập nhật
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
    
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }

    // --- SỬA GETTER/SETTER ---
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    // -------------------------

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
}