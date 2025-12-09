package com.hotel.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {
    private Long id;
    private Long guestId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalAmount;
    private String status;

    // --- CÁC TRƯỜNG MỚI BỔ SUNG ---
    private Integer adults;             // Số người lớn
    private Integer childrenUnder3;     // Trẻ em < 3 tuổi
    private Integer children3To5;       // Trẻ em 3-5 tuổi
    private Integer children6To12;      // Trẻ em 6-12 tuổi
    private Integer preferredFloor;     // Tầng khách muốn (Optional)

    public Booking() {
    }

    // Constructor đầy đủ (Update thêm tham số mới vào)
    public Booking(Long id, Long guestId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate, 
                   BigDecimal totalAmount, String status,
                   Integer adults, Integer childrenUnder3, Integer children3To5, Integer children6To12, Integer preferredFloor) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.adults = adults;
        this.childrenUnder3 = childrenUnder3;
        this.children3To5 = children3To5;
        this.children6To12 = children6To12;
        this.preferredFloor = preferredFloor;
    }

    // --- GETTERS & SETTERS CŨ (Giữ nguyên) ---
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

    // --- GETTERS & SETTERS MỚI ---
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