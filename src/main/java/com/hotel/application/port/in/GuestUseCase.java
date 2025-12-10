package com.hotel.application.port.in;

import com.hotel.domain.model.Guest;
import java.util.List;

public interface GuestUseCase {
    Guest createGuest(Guest guest);
    List<Guest> getAllGuests();
    Guest getGuestByUserId(Long userId);
    Guest getGuestById(Long id);    // Để Controller tìm xem khách có tồn tại k
    Guest updateGuest(Guest guest); // Để Controller lưu thông tin mới
}