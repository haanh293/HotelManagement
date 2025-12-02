package com.hotel.application.port.out;

import com.hotel.domain.model.Booking;
import java.util.List;
import java.util.Optional;
public interface BookingRepositoryPort {
    Booking save(Booking booking);
    List<Booking> findAll();
    Optional<Booking> findById(Long id);
    List<Booking> findByGuestId(Long guestId);
}