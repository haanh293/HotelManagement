package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.RoomUseCase;
import com.hotel.domain.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms") 
public class RoomController {

    private final RoomUseCase roomUseCase;

    // --- CONSTRUCTOR THỦ CÔNG (SỬA LỖI NÀY LÀ XONG) ---
    public RoomController(RoomUseCase roomUseCase) {
        this.roomUseCase = roomUseCase;
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
}