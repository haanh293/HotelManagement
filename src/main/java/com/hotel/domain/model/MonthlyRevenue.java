package com.hotel.domain.model;

public class MonthlyRevenue {
    private int month;      // Tháng
    private int year;       // Năm
    private double revenue; // Doanh thu

    public MonthlyRevenue(int month, int year, double revenue) {
        this.month = month;
        this.year = year;
        this.revenue = revenue;
    }

    // Getters
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public double getRevenue() { return revenue; }
}