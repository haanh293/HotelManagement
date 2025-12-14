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

    public Booking createBooking(Booking booking) {
        // --- 1. VALIDATION MỚI (Nên thêm) ---
        // Đảm bảo khách đã chọn loại phòng
        if (booking.getRoomType() == null || booking.getRoomType().isEmpty()) {
            throw new RuntimeException("Vui lòng chọn loại phòng (roomType)!");
        }
        
        // (Tùy chọn) Kiểm tra các trường khác nếu cần
        // if (booking.getViewType() == null) ...
        // ------------------------------------

        // 2. Kiểm tra phòng trống
        boolean isAvailable = bookingRepositoryPort.isRoomAvailable(
                booking.getRoomId(), 
                booking.getCheckInDate(), 
                booking.getCheckOutDate()
        );

        if (!isAvailable) {
            throw new RuntimeException("Phòng đã có người đặt trong thời gian này!");
        }

        // 3. Lưu
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