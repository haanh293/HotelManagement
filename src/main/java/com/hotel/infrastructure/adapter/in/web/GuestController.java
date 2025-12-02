package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.GuestUseCase;
import com.hotel.domain.model.Guest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestUseCase guestUseCase;

    public GuestController(GuestUseCase guestUseCase) {
        this.guestUseCase = guestUseCase;
    }

    @PostMapping
    public ResponseEntity<Guest> createGuest(@RequestBody Guest guest) {
        return ResponseEntity.ok(guestUseCase.createGuest(guest));
    }

    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestUseCase.getAllGuests());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<Guest> getGuestByUserId(@PathVariable Long userId) {
        Guest guest = guestUseCase.getGuestByUserId(userId);
        if (guest != null) {
            return ResponseEntity.ok(guest);
        } else {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu chưa cập nhật hồ sơ
        }
    }
}