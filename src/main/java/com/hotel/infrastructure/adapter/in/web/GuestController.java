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

    // 1. Tạo mới
    @PostMapping
    public ResponseEntity<Guest> createGuest(@RequestBody Guest guest) {
        return ResponseEntity.ok(guestUseCase.createGuest(guest));
    }

    // 2. Lấy tất cả
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestUseCase.getAllGuests());
    }

    // 3. Xem Profile (Theo UserID - Dùng cho trang "Hồ sơ của tôi")
    // API này giờ đã trả về cả TotalSpending, DOB, Gender
    @GetMapping("/user/{userId}")
    public ResponseEntity<Guest> getGuestByUserId(@PathVariable Long userId) {
        Guest guest = guestUseCase.getGuestByUserId(userId);
        if (guest != null) {
            return ResponseEntity.ok(guest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. --- API MỚI: CẬP NHẬT PROFILE ---
    // URL: PUT /api/guests/{id} (ID này là Guest ID, không phải User ID)
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @RequestBody Guest updatedInfo) {
        // B1: Tìm xem khách hàng có tồn tại không
        Guest existingGuest = guestUseCase.getGuestById(id);
        
        if (existingGuest == null) {
            return ResponseEntity.notFound().build();
        }

        // B2: Cập nhật các thông tin cho phép sửa
        existingGuest.setFullName(updatedInfo.getFullName());
        existingGuest.setPhoneNumber(updatedInfo.getPhoneNumber());
        existingGuest.setAddress(updatedInfo.getAddress());
        
        
        existingGuest.setDateOfBirth(updatedInfo.getDateOfBirth());
        existingGuest.setGender(updatedInfo.getGender());

        // B3: Lưu lại xuống DB
        Guest savedGuest = guestUseCase.updateGuest(existingGuest);
        
        return ResponseEntity.ok(savedGuest);
    }
}