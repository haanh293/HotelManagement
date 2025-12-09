package com.hotel.domain.model;

import java.math.BigDecimal;

public class Room {
    private Long id;
    private String name;
    private String type;        // DELUXE, LUXURY
    private BigDecimal price;
    private String status;      // AVAILABLE, BOOKED
    private String description; // Mô tả tiện nghi (Wifi, Tivi...)

    // --- CÁC TRƯỜNG MỚI BỔ SUNG (THEO YÊU CẦU) ---
    private Long hotelId;       // Thuộc khách sạn nào (Đà Lạt, Hà Nội...)
    private Integer floor;      // Tầng mấy (Để tính "Tầng 3-6 còn bao nhiêu phòng")
    private String viewType;    // Hướng nhìn: LAKE (Hồ), MOUNTAIN (Núi), CITY (Phố)
    private String position;    // Vị trí: NEAR_ELEVATOR, CORNER (Góc)...
    private String lightType;   // Ánh sáng: DAYLIGHT, SOFT...

    public Room() {
    }

    // Constructor đầy đủ
    public Room(Long id, String name, String type, BigDecimal price, String status, String description, 
                Long hotelId, Integer floor, String viewType, String position, String lightType) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.status = status;
        this.description = description;
        this.hotelId = hotelId;
        this.floor = floor;
        this.viewType = viewType;
        this.position = position;
        this.lightType = lightType;
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Getter/Setter cho trường mới
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public String getViewType() { return viewType; }
    public void setViewType(String viewType) { this.viewType = viewType; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getLightType() { return lightType; }
    public void setLightType(String lightType) { this.lightType = lightType; }
}