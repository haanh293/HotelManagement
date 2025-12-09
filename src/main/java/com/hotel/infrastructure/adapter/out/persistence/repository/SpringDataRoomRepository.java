package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataRoomRepository extends JpaRepository<RoomJpaEntity, Long> {

    // --- CÂU QUERY LỌC NÂNG CAO ---
    // Logic: Nếu tham số truyền vào là NULL thì bỏ qua điều kiện đó (coi như chọn tất cả)
    @Query("SELECT r FROM RoomJpaEntity r WHERE " +
           "(:hotelId IS NULL OR r.hotelId = :hotelId) AND " +
           "(:viewType IS NULL OR r.viewType = :viewType) AND " +
           "(:position IS NULL OR r.position = :position) AND " +
           "(:lightType IS NULL OR r.lightType = :lightType) AND " +
           "(:minFloor IS NULL OR r.floor >= :minFloor) AND " +
           "(:maxFloor IS NULL OR r.floor <= :maxFloor) AND " +
           "(r.status = 'AVAILABLE')")
    List<RoomJpaEntity> searchRooms(@Param("hotelId") Long hotelId,
                                    @Param("viewType") String viewType,
                                    @Param("position") String position,
                                    @Param("lightType") String lightType,
                                    @Param("minFloor") Integer minFloor,
                                    @Param("maxFloor") Integer maxFloor);
}