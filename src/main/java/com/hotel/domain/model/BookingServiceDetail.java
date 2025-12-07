package com.hotel.domain.model;

public class BookingServiceDetail {
    private Long id;
    private Long bookingId; // Gắn với đơn đặt phòng nào
    private Long serviceId; // Dùng dịch vụ gì (Spa, Giặt là...)
    private Integer quantity; // Số lượng bao nhiêu

    public BookingServiceDetail() {}

    public BookingServiceDetail(Long id, Long bookingId, Long serviceId, Integer quantity) {
        this.id = id;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}