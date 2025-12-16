package com.hotel.domain.model;

public enum InvoiceStatus {
    UNPAID,     // Chưa thanh toán
    PAID,       // Đã thanh toán
    PENDING,    // Đang chờ xử lý
    CANCELLED   // Đã hủy
}