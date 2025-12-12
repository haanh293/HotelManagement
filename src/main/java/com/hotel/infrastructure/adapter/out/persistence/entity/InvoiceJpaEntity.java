package com.hotel.infrastructure.adapter.out.persistence.entity;

import com.hotel.domain.model.InvoiceStatus; // Import Enum vừa tạo
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class InvoiceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private Double totalAmount;
    private LocalDate paymentDate;
    private String paymentMethod;

    // 1. Thêm cấu hình mapping cho cột status
    @Enumerated(EnumType.STRING) // Lưu chữ "PAID", "UNPAID" vào DB cho dễ đọc
    @Column(nullable = false)    // Không cho phép null
    private InvoiceStatus status;

    public InvoiceJpaEntity() {
        // Mặc định DB
        this.status = InvoiceStatus.UNPAID;
    }

    // 2. Thêm Getter & Setter cho status
    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }

    // Các Getter/Setter cũ giữ nguyên...
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