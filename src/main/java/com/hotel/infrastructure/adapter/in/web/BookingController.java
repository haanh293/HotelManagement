package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.BookingUseCase;
import com.hotel.domain.model.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingUseCase bookingUseCase;

    public BookingController(BookingUseCase bookingUseCase) {
        this.bookingUseCase = bookingUseCase;
    }

    @PostMapping
    // Lưu ý: Đổi kiểu trả về thành ResponseEntity<?> để trả về được cả Booking (khi thành công) hoặc String (khi lỗi)
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            // Cố gắng tạo đặt phòng
            Booking newBooking = bookingUseCase.createBooking(booking);
            return ResponseEntity.ok(newBooking);
            
        } catch (RuntimeException e) {
            // Nếu bên Service "la lên" (throw Exception) là phòng trùng
            // Thì Controller bắt lấy và trả về lỗi 400 kèm lời nhắn
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingUseCase.getAllBookings());
    }
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Booking>> getHistory(@PathVariable Long guestId) {
        return ResponseEntity.ok(bookingUseCase.getBookingsByGuest(guestId));
    }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        // Gọi UseCase chứ không gọi Repo trực tiếp
        bookingUseCase.cancelBooking(id);
        return ResponseEntity.ok().build();
    }
    
}