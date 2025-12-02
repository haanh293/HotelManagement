package com.hotel.application.port.in;

import com.hotel.domain.model.Room;
import java.util.List;

public interface RoomUseCase {
	Room createRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    void deleteRoom(Long id);
}
