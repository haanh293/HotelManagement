package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.BookingUseCase;
import com.hotel.domain.model.Booking;
import com.hotel.domain.model.BookingServiceDetail;
import com.hotel.infrastructure.adapter.out.persistence.entity.BookingServiceJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingServiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingUseCase bookingUseCase;
    private final SpringDataBookingServiceRepository bookingServiceRepo;

    // Constructor nhận cả 2 biến (Để tránh lỗi null)
    public BookingController(BookingUseCase bookingUseCase, 
                             SpringDataBookingServiceRepository bookingServiceRepo) {
        this.bookingUseCase = bookingUseCase;
        this.bookingServiceRepo = bookingServiceRepo;
    }

    // 1. API Tạo đặt phòng (Có bắt lỗi trùng phòng)
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking newBooking = bookingUseCase.createBooking(booking);
            return ResponseEntity.ok(newBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. API Lấy danh sách đặt phòng
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingUseCase.getAllBookings());
    }

    // 3. API Hủy đặt phòng
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingUseCase.cancelBooking(id);
        return ResponseEntity.ok().build();
    }

    // 4. API Xem lịch sử khách
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Booking>> getHistory(@PathVariable Long guestId) {
        return ResponseEntity.ok(bookingUseCase.getBookingsByGuest(guestId));
    }


    // 5. Thêm dịch vụ vào đơn đặt phòng
    // URL: POST /api/bookings/1/services
    @PostMapping("/{bookingId}/services")
    public ResponseEntity<?> addServiceToBooking(@PathVariable Long bookingId, 
                                                 @RequestBody BookingServiceDetail detail) {
        BookingServiceJpaEntity entity = new BookingServiceJpaEntity();
        entity.setBookingId(bookingId);
        entity.setServiceId(detail.getServiceId());
        entity.setQuantity(detail.getQuantity());

        bookingServiceRepo.save(entity);
        return ResponseEntity.ok("Đã thêm dịch vụ thành công!");
    }

    // 6. Xem đơn phòng này đã dùng những dịch vụ gì
    // URL: GET /api/bookings/1/services
    @GetMapping("/{bookingId}/services")
    public ResponseEntity<List<BookingServiceJpaEntity>> getServicesByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingServiceRepo.findByBookingId(bookingId));
    }
}