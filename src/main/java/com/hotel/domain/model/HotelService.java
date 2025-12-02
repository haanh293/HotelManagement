package com.hotel.domain.model;

public class HotelService {
    private Long id;
    private String name;    // Tên dịch vụ (Giặt ủi, Spa...)
    private Double price;   // Giá tiền
    private String unit;    // Đơn vị (Lần, Kg, Vé...)
    private String status;  // Active/Inactive

    public HotelService() {}

    public HotelService(Long id, String name, Double price, String unit, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}