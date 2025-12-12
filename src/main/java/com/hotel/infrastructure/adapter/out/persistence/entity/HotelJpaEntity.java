package com.hotel.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "hotels")
public class HotelJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;    // Quan trọng: Dùng để lọc theo Tỉnh/Thành
    private String address;
    
    @Column(length = 1000) // Cho phép mô tả dài hơn
    private String description;
    @Column(name = "basic_price", precision = 19, scale = 2) 
    private BigDecimal basicPrice;
    public HotelJpaEntity() {}

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
    public BigDecimal getBasicPrice() { return basicPrice; }
    public void setBasicPrice(BigDecimal basicPrice) { this.basicPrice = basicPrice; }
}