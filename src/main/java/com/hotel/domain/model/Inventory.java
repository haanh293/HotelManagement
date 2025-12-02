package com.hotel.domain.model;

public class Inventory {
    private Long id;
    private String name;      // Tên kho (Kho chăn, Kho gối...)
    private String type;      // Loại kho (Đồ dùng, Thực phẩm...)
    private Integer stock;    // Số lượng tồn
    private String location;  // Vị trí (Khu A, Tầng 2...)

    public Inventory() {}

    public Inventory(Long id, String name, String type, Integer stock, String location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.location = location;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}