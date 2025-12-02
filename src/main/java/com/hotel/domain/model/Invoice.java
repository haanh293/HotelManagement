package com.hotel.domain.model;

import java.time.LocalDate;

public class Invoice {
    private Long id;
    private Long bookingId;      // Mã đặt phòng
    private Double totalAmount;  // Tổng tiền
    private LocalDate paymentDate; // Ngày thanh toán
    private String paymentMethod;  // Tiền mặt/Thẻ

    public Invoice() {}

    public Invoice(Long id, Long bookingId, Double totalAmount, LocalDate paymentDate, String paymentMethod) {
        this.id = id;
        this.bookingId = bookingId;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}