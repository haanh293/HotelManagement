package com.hotel.application.port.in;

import com.hotel.domain.model.Booking;
import java.util.List;

public interface BookingUseCase {
    Booking createBooking(Booking booking);
    List<Booking> getAllBookings();
    void cancelBooking(Long id);
    List<Booking> getBookingsByGuest(Long guestId);
    Booking getBookingByCode(String bookingCode);
    Booking updateBookingStatus(Long id, String status);
}