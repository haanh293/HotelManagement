package com.hotel.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class BookingJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nối với bảng Guest
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private GuestJpaEntity guest;

    // Nối với bảng Room
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomJpaEntity room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalAmount;
    private String status;

    public BookingJpaEntity() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public GuestJpaEntity getGuest() { return guest; }
    public void setGuest(GuestJpaEntity guest) { this.guest = guest; }
    public RoomJpaEntity getRoom() { return room; }
    public void setRoom(RoomJpaEntity room) { this.room = room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}