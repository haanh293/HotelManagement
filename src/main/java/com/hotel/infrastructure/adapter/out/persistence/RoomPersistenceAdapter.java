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

    private final SpringDataRoomRepository roomRepository;

    public RoomPersistenceAdapter(SpringDataRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room save(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity();
        
        // Map dữ liệu cũ
        entity.setId(room.getId());
        entity.setName(room.getName());
        entity.setType(room.getType());
        entity.setPrice(room.getPrice());
        entity.setStatus(room.getStatus());
        entity.setDescription(room.getDescription());

        // --- MAP DỮ LIỆU MỚI (QUAN TRỌNG) ---
        entity.setHotelId(room.getHotelId());
        entity.setFloor(room.getFloor());
        entity.setViewType(room.getViewType());
        entity.setPosition(room.getPosition());
        entity.setLightType(room.getLightType());
        // -------------------------------------

        RoomJpaEntity savedEntity = roomRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id).map(this::mapToDomain);
    }
    @Override
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

    // Hàm chuyển đổi từ Entity (DB) sang Domain (Code)
    private Room mapToDomain(RoomJpaEntity entity) {
        return new Room(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getPrice(),
                entity.getStatus(),
                entity.getDescription(),
                // --- THÊM THAM SỐ MỚI VÀO CONSTRUCTOR ---
                entity.getHotelId(),
                entity.getFloor(),
                entity.getViewType(),
                entity.getPosition(),
                entity.getLightType()
        );
    }
}