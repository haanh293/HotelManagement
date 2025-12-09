package com.hotel.domain.model;

public class Hotel {
    private Long id;
    private String name;        // Tên khách sạn (VD: Imperial Đà Lạt)
    private String city;        // Thành phố (Để tìm kiếm theo khu vực)
    private String address;     // Địa chỉ cụ thể
    private String description; // Mô tả chung

    public Hotel() {}

    public Hotel(Long id, String name, String city, String address, String description) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.description = description;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}