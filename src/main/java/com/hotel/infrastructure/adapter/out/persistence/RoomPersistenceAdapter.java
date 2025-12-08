package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.RoomRepositoryPort;
import com.hotel.domain.model.Room;
import com.hotel.infrastructure.adapter.out.persistence.entity.RoomJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataRoomRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RoomPersistenceAdapter implements RoomRepositoryPort {

    private final SpringDataRoomRepository springDataRoomRepository;

    // --- CONSTRUCTOR THỦ CÔNG  ---
    public RoomPersistenceAdapter(SpringDataRoomRepository springDataRoomRepository) {
        this.springDataRoomRepository = springDataRoomRepository;
    }
    // ----------------------------------------

    @Override
    public Room save(Room room) {
        RoomJpaEntity entity = mapToJpaEntity(room);
        RoomJpaEntity savedEntity = springDataRoomRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public List<Room> findAll() {
        return springDataRoomRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> findById(Long id) {
        return springDataRoomRepository.findById(id).map(this::mapToDomain);
    }
    

    @Override
    public void deleteById(Long id) {
        springDataRoomRepository.deleteById(id);
    }

    private RoomJpaEntity mapToJpaEntity(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity();
        entity.setId(room.getId()); 
        entity.setName(room.getName());
        entity.setType(room.getType());
        entity.setPrice(room.getPrice());
        entity.setStatus(room.getStatus());
        entity.setDescription(room.getDescription());
        return entity;
    }

    private Room mapToDomain(RoomJpaEntity entity) {
        // Nếu Constructor Room(...) báo đỏ, xem bước 2
        return new Room(entity.getId(), entity.getName(), entity.getType(), 
                        entity.getPrice(), entity.getStatus(), entity.getDescription());
    }
}
