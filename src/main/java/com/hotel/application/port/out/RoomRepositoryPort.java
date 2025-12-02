package com.hotel.application.port.out;

import com.hotel.domain.model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepositoryPort {
	Room save(Room room);
    List<Room> findAll();
    Optional<Room> findById(Long id);
    void deleteById(Long id);
}
