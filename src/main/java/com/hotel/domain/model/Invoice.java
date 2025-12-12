package com.hotel.domain.model;

import java.time.LocalDate;

public class Invoice {
    private Long id;
    private Long bookingId;
    private Double totalAmount;
    private LocalDate paymentDate;
    private String paymentMethod;
    // 1. Thêm trường status
    private InvoiceStatus status; 

    public Invoice() {
        // Mặc định khi khởi tạo mới là chưa thanh toán
        this.status = InvoiceStatus.UNPAID; 
    }

    // Cập nhật Constructor
    public Invoice(Long id, Long bookingId, Double totalAmount, LocalDate paymentDate, String paymentMethod, InvoiceStatus status) {
        this.id = id;
        this.bookingId = bookingId;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = (status != null) ? status : InvoiceStatus.UNPAID;
    }


    // 2. Thêm Getter & Setter cho status
    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
    
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