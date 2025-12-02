package com.hotel.application.port.in;

import com.hotel.domain.model.Guest;
import java.util.List;

public interface GuestUseCase {
    Guest createGuest(Guest guest);
    List<Guest> getAllGuests();
    Guest getGuestByUserId(Long userId);
}