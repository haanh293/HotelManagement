package com.hotel.domain.model;
import java.math.BigDecimal;
import java.time.LocalDate;
public class Guest {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private BigDecimal totalSpending;
    public Guest() {}

    public Guest(Long id, String fullName, String phoneNumber, String email, String address, LocalDate dateOfBirth, String gender, BigDecimal totalSpending) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.totalSpending = totalSpending;
    }
    private Long userId; // Liên kết với bảng User
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public BigDecimal getTotalSpending() { return totalSpending; }
    public void setTotalSpending(BigDecimal totalSpending) { this.totalSpending = totalSpending; }
}