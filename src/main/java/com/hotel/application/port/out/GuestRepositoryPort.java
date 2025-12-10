package com.hotel.application.port.out;

import com.hotel.domain.model.Guest;
import java.util.List;

public interface GuestRepositoryPort {
    Guest save(Guest guest);
    List<Guest> findAll();
    Guest getGuestByUserId(Long userId);
    java.util.Optional<Guest> findById(Long id);
}