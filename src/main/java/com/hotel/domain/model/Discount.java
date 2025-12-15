package com.hotel.domain.model;

import java.time.LocalDate;

public class Discount {
    private Long id;
    private String code;          // Mã (VD: SALE50)
    private Double value;         // Giá trị giảm (VD: 50000 hoặc 10%)
    private LocalDate startDate;  // Ngày bắt đầu
    private LocalDate endDate;    // Ngày kết thúc
    private Long guestId; 
    private Boolean isUsed;
    private Integer quantity;       // Số lượng mã còn lại
    private String description;
    public Discount() {}

    public Discount(Long id, String code, Double value, LocalDate startDate, LocalDate endDate, Long guestId, Boolean isUsed, Integer quantity, String description) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestId = guestId;
        this.isUsed = isUsed;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters & Setters
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