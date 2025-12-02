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
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingUseCase.createBooking(booking));
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