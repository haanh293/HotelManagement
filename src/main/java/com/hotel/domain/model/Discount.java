package com.hotel.domain.model;

import java.time.LocalDate;

public class Discount {
    private Long id;
    private String code;          // Mã (VD: SALE50)
    private Double value;         // Giá trị giảm (VD: 50000 hoặc 10%)
    private LocalDate startDate;  // Ngày bắt đầu
    private LocalDate endDate;    // Ngày kết thúc

    public Discount() {}

    public Discount(Long id, String code, Double value, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
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
}