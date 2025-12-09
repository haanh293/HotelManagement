package com.hotel.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class BookingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long guestId;
    
    // Nếu bạn dùng quan hệ object (ManyToOne) thì giữ nguyên, còn nếu dùng ID thì để Long
    // Ở đây tôi để quan hệ Object như lúc nãy bạn báo lỗi để bạn dễ sửa lại nếu cần
    // Nhưng để đơn giản và khớp với Domain, tôi dùng roomId (Long)
    @Column(name = "room_id")
    private Long roomId; 

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalAmount;
    private String status;

    // --- CÁC CỘT MỚI ---
    private Integer adults;
    private Integer childrenUnder3;
    private Integer children3To5;
    private Integer children6To12;
    private Integer preferredFloor;

    public BookingJpaEntity() {
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Getter/Setter Mới
    public Integer getAdults() { return adults; }
    public void setAdults(Integer adults) { this.adults = adults; }
    public Integer getChildrenUnder3() { return childrenUnder3; }
    public void setChildrenUnder3(Integer childrenUnder3) { this.childrenUnder3 = childrenUnder3; }
    public Integer getChildren3To5() { return children3To5; }
    public void setChildren3To5(Integer children3To5) { this.children3To5 = children3To5; }
    public Integer getChildren6To12() { return children6To12; }
    public void setChildren6To12(Integer children6To12) { this.children6To12 = children6To12; }
    public Integer getPreferredFloor() { return preferredFloor; }
    public void setPreferredFloor(Integer preferredFloor) { this.preferredFloor = preferredFloor; }
}