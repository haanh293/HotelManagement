package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.RoomUseCase;
import com.hotel.domain.model.Room;
import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.BookingServiceJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.RoomJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingServiceRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataRoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomUseCase roomUseCase;
    private final SpringDataBookingRepository bookingRepo;
    private final SpringDataBookingServiceRepository bookingServiceRepo;
    private final SpringDataRoomRepository roomRepoRaw; // <--- Biến mới để search

    // Cập nhật Constructor đầy đủ 4 biến
    public RoomController(RoomUseCase roomUseCase,
                          SpringDataBookingRepository bookingRepo,
                          SpringDataBookingServiceRepository bookingServiceRepo,
                          SpringDataRoomRepository roomRepoRaw) {
        this.roomUseCase = roomUseCase;
        this.bookingRepo = bookingRepo;
        this.bookingServiceRepo = bookingServiceRepo;
        this.roomRepoRaw = roomRepoRaw;
    }

    // --- API CŨ ---
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomUseCase.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomUseCase.getRoomById(id);
        return (room != null) ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{roomId}/current-services")
    public ResponseEntity<?> getCurrentServicesOfRoom(@PathVariable Long roomId) {
        var activeBookingOpt = bookingRepo.findActiveBookingByRoom(roomId);
        if (activeBookingOpt.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        Long bookingId = activeBookingOpt.get().getId();
        List<BookingServiceJpaEntity> services = bookingServiceRepo.findByBookingId(bookingId);
        return ResponseEntity.ok(services);
    }

    // --- API MỚI: TÌM KIẾM PHÒNG NÂNG CAO ---
    // URL ví dụ: /api/rooms/search?hotelId=1&viewType=LAKE&minFloor=3&maxFloor=6
    @GetMapping("/search")
    public ResponseEntity<List<RoomJpaEntity>> searchRooms(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) String viewType,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String lightType,
            @RequestParam(required = false) Integer minFloor,
            @RequestParam(required = false) Integer maxFloor
    ) {
        // Gọi thẳng xuống Repository để lấy dữ liệu lọc
        return ResponseEntity.ok(roomRepoRaw.searchRooms(hotelId, viewType, position, lightType, minFloor, maxFloor));
    }
}