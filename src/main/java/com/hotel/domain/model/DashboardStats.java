package com.hotel.domain.model;

public class DashboardStats {
    private long totalRooms;
    private long totalGuests;
    private long totalBookings;
    private double totalRevenue; // Tổng doanh thu

    public DashboardStats(long totalRooms, long totalGuests, long totalBookings, double totalRevenue) {
        this.totalRooms = totalRooms;
        this.totalGuests = totalGuests;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
    }
    
    // Getters
    public long getTotalRooms() { return totalRooms; }
    public long getTotalGuests() { return totalGuests; }
    public long getTotalBookings() { return totalBookings; }
    public double getTotalRevenue() { return totalRevenue; }
}