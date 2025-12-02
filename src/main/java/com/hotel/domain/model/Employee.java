package com.hotel.domain.model;

public class Employee {
    private Long id;
    private String fullName;
    private String position; // Chức vụ (Lễ tân, Quản lý...)
    private String phoneNumber;
    private Double salary;   // Lương

    public Employee() {}

    public Employee(Long id, String fullName, String position, String phoneNumber, Double salary) {
        this.id = id;
        this.fullName = fullName;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
    }
    private Long userId; // Liên kết với bảng User

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}