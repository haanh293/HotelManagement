package com.hotel.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "discounts")
public class DiscountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;        // Mã giảm giá (VD: SALE50)

    private Double value;       // Giá trị giảm (VD: 0.1 là 10%, hoặc 100000 là tiền mặt - tùy logic bạn)
    
    private LocalDate startDate;
    private LocalDate endDate;


    @Column(name = "guest_id")
    private Long guestId;       // Quan trọng: Nếu NULL -> Ai cũng dùng được. Nếu có ID -> Chỉ người đó dùng được.

    @Column(name = "is_used")
    private Boolean isUsed = false; // Đánh dấu đã dùng chưa (Mặc định là False)
    
    @Column(name = "quantity")
    private Integer quantity;       // Lưu số lượng còn lại

    @Column(name = "description")   // Lưu mô tả chi tiết
    private String description;

    public DiscountJpaEntity() {}

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    
    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }

    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) { this.isUsed = isUsed; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}