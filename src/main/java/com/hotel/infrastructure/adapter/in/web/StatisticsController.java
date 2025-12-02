package com.hotel.infrastructure.adapter.in.web;

import com.hotel.domain.model.DashboardStats;
import com.hotel.infrastructure.adapter.out.persistence.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final SpringDataRoomRepository roomRepo;
    private final SpringDataGuestRepository guestRepo;
    private final SpringDataBookingRepository bookingRepo;
    private final SpringDataInvoiceRepository invoiceRepo;

    public StatisticsController(SpringDataRoomRepository roomRepo,
                                SpringDataGuestRepository guestRepo,
                                SpringDataBookingRepository bookingRepo,
                                SpringDataInvoiceRepository invoiceRepo) {
        this.roomRepo = roomRepo;
        this.guestRepo = guestRepo;
        this.bookingRepo = bookingRepo;
        this.invoiceRepo = invoiceRepo;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        // 1. Đếm số lượng
        long totalRooms = roomRepo.count();
        long totalGuests = guestRepo.count();
        long totalBookings = bookingRepo.count();

        // 2. Tính tổng doanh thu từ bảng Invoices
        // (Duyệt qua danh sách hóa đơn, cộng dồn cột totalAmount)
        double totalRevenue = invoiceRepo.findAll().stream()
                .mapToDouble(invoice -> invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0)
                .sum();

        DashboardStats stats = new DashboardStats(totalRooms, totalGuests, totalBookings, totalRevenue);
        
        return ResponseEntity.ok(stats);
    }
}