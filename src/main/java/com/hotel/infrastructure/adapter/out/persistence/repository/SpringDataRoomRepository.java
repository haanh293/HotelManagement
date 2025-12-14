package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataRoomRepository extends JpaRepository<RoomJpaEntity, Long> {

    // --- CÂU QUERY ĐÃ SỬA (Xóa các trường view, position, light) ---
    @Query("SELECT r FROM RoomJpaEntity r WHERE " +
           "(:hotelId IS NULL OR r.hotelId = :hotelId) AND " +
           "(:minFloor IS NULL OR r.floor >= :minFloor) AND " +
           "(:maxFloor IS NULL OR r.floor <= :maxFloor) AND " +
           "(r.status = 'AVAILABLE')")
    List<RoomJpaEntity> searchRooms(
           @Param("hotelId") Long hotelId,
           @Param("minFloor") Integer minFloor,
           @Param("maxFloor") Integer maxFloor
    );
}