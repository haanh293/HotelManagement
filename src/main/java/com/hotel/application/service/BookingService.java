package com.hotel.application.service;

import com.hotel.application.port.in.BookingUseCase;
import com.hotel.application.port.out.BookingRepositoryPort;
import com.hotel.domain.model.Booking;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService implements BookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;

    public BookingService(BookingRepositoryPort bookingRepositoryPort) {
        this.bookingRepositoryPort = bookingRepositoryPort;
    }

    @Override
    public Booking createBooking(Booking booking) {
        // Có thể thêm logic tính tiền tự động ở đây
        booking.setStatus("CONFIRMED");
        return bookingRepositoryPort.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepositoryPort.findAll();
    }
    @Override
    public void cancelBooking(Long id) {
        // 1. Tìm đặt phòng theo ID
        Booking booking = bookingRepositoryPort.findById(id).orElse(null);
        
        // 2. Nếu tìm thấy thì đổi trạng thái
        if (booking != null) {
            booking.setStatus("CANCELLED");
            bookingRepositoryPort.save(booking);
        }
    }
    @Override
    public List<Booking> getBookingsByGuest(Long guestId) {
        return bookingRepositoryPort.findByGuestId(guestId);
    }
}