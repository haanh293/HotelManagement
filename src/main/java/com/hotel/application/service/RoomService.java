package com.hotel.application.service;

import com.hotel.application.port.in.RoomUseCase;
import com.hotel.application.port.out.RoomRepositoryPort;
import com.hotel.domain.model.Room;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class RoomService implements RoomUseCase{
	private final RoomRepositoryPort roomRepositoryPort;
	public RoomService(RoomRepositoryPort roomRepositoryPort) {
        this.roomRepositoryPort = roomRepositoryPort;
    }
    @Override
    public Room createRoom(Room room) {
        return roomRepositoryPort.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepositoryPort.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepositoryPort.findById(id).orElse(null);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepositoryPort.deleteById(id);
    }
}
