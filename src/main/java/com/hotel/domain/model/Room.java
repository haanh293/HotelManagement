package com.hotel.domain.model;

public class Room {
    private Long id;
    private String name;
    private String type;
    private Double price;
    private String status;
    private String description;

    // Constructor rỗng
    public Room() {
    }

    // Constructor đầy đủ
    public Room(Long id, String name, String type, Double price, String status, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.status = status;
        this.description = description;
    }

    // --- GETTERS VÀ SETTERS (Bắt buộc phải có) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}