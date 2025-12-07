package com.hotel.infrastructure.adapter.in.web;

import com.hotel.domain.model.DashboardStats;
import com.hotel.domain.model.MonthlyRevenue; // Import mới
import com.hotel.infrastructure.adapter.out.persistence.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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

    // API 1: Thống kê tổng quan
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        long totalRooms = roomRepo.count();
        long totalGuests = guestRepo.count();
        long totalBookings = bookingRepo.count();
        double totalRevenue = invoiceRepo.findAll().stream()
                .mapToDouble(invoice -> invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0)
                .sum();
        return ResponseEntity.ok(new DashboardStats(totalRooms, totalGuests, totalBookings, totalRevenue));
    }

    // API: Dữ liệu biểu đồ doanh thu theo tháng
    @GetMapping("/revenue-chart")
    public ResponseEntity<List<MonthlyRevenue>> getRevenueChart() {
        return ResponseEntity.ok(invoiceRepo.getMonthlyRevenue());
    }
}