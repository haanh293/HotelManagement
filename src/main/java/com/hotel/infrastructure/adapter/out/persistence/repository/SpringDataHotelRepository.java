package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.HotelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataHotelRepository extends JpaRepository<HotelJpaEntity, Long> {
    // Hàm tìm kiếm khách sạn theo thành phố
    List<HotelJpaEntity> findByCity(String city);
}