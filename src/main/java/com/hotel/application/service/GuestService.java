package com.hotel.application.service;

import com.hotel.application.port.in.GuestUseCase;
import com.hotel.application.port.out.GuestRepositoryPort;
import com.hotel.domain.model.Guest;
import org.springframework.stereotype.Service;
import java.util.List;

@org.springframework.stereotype.Service
public class GuestService implements GuestUseCase {

	private final com.hotel.application.port.out.GuestRepositoryPort guestRepositoryPort;

    public GuestService(GuestRepositoryPort guestRepositoryPort) {
        this.guestRepositoryPort = guestRepositoryPort;
    }

    @Override
    public Guest createGuest(Guest guest) {
        return guestRepositoryPort.save(guest);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepositoryPort.findAll();
    }
    @Override
    public Guest getGuestByUserId(Long userId) {
        // Gọi xuống tầng dưới để tìm
        return guestRepositoryPort.getGuestByUserId(userId);
    }
    @Override
    public Guest getGuestById(Long id) {
        // Gọi xuống Adapter thông qua Port
        return guestRepositoryPort.findById(id).orElse(null);
    }
    @Override
    public Guest updateGuest(Guest guest) {
        // Bản chất update cũng là save đè lên cái cũ
        return guestRepositoryPort.save(guest);
    }
}