package com.hotel.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"guest_id", "hotel_id"}) // Một người chỉ thích 1 khách sạn 1 lần
})
public class FavoriteJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_id", nullable = false)
    private Long guestId; // Ai thích

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId; // Thích khách sạn nào

    public FavoriteJpaEntity() {}
    
    // Constructor tiện lợi
    public FavoriteJpaEntity(Long guestId, Long hotelId) {
        this.guestId = guestId;
        this.hotelId = hotelId;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
}