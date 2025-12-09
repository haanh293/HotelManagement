package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.RoomUseCase;
import com.hotel.domain.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.BookingServiceJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingServiceRepository;

@RestController
@RequestMapping("/api/rooms") 
public class RoomController {

    private final RoomUseCase roomUseCase;
    private final SpringDataBookingRepository bookingRepo;
    private final SpringDataBookingServiceRepository bookingServiceRepo;
    public RoomController(RoomUseCase roomUseCase,
            SpringDataBookingRepository bookingRepo,
            SpringDataBookingServiceRepository bookingServiceRepo) {
    	this.roomUseCase = roomUseCase;
    	this.bookingRepo = bookingRepo;
    	this.bookingServiceRepo = bookingServiceRepo;
    }
    // --------------------------------------------------

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomUseCase.createRoom(room));
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomUseCase.getAllRooms());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomUseCase.deleteRoom(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomUseCase.getRoomById(id);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // --- API: Xem dịch vụ đang sử dụng của 1 phòng ---
    // URL: GET /api/rooms/{roomId}/current-services
    @GetMapping("/{roomId}/current-services")
    public ResponseEntity<?> getCurrentServicesOfRoom(@PathVariable Long roomId) {
        // Bước 1: Tìm xem phòng này có ai đang ở không (CHECKED_IN)
        var activeBookingOpt = bookingRepo.findActiveBookingByRoom(roomId);

        if (activeBookingOpt.isEmpty()) {
            // Nếu không có ai ở -> Trả về danh sách rỗng (hoặc thông báo)
            return ResponseEntity.ok(List.of()); 
        }

        // Bước 2: Nếu có người ở -> Lấy ID đơn đặt phòng
        Long bookingId = activeBookingOpt.get().getId();

        // Bước 3: Lấy danh sách dịch vụ theo bookingId
        List<BookingServiceJpaEntity> services = bookingServiceRepo.findByBookingId(bookingId);

        return ResponseEntity.ok(services);
    }
}