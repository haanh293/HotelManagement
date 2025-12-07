package com.hotel.domain.model;

import java.time.LocalDate;

public class Review {
    private Long id;
    private Long guestId; // Ai đánh giá
    private Long roomId;  // Đánh giá phòng nào
    private Integer rating; // 1 đến 5 sao
    private String comment; // Nội dung
    private LocalDate reviewDate; // Ngày đánh giá

    public Review() {}

    public Review(Long id, Long guestId, Long roomId, Integer rating, String comment, LocalDate reviewDate) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
    
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
}