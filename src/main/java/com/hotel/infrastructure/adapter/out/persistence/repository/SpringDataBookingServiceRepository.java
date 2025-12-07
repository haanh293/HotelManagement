package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.BookingServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataBookingServiceRepository extends JpaRepository<BookingServiceJpaEntity, Long> {
    // Hàm tìm tất cả dịch vụ của một đơn đặt phòng
    List<BookingServiceJpaEntity> findByBookingId(Long bookingId);
}