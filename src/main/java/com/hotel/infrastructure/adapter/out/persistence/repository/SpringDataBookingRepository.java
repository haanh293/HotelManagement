package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface SpringDataBookingRepository extends JpaRepository<BookingJpaEntity, Long> {
	// Tìm các đơn đặt phòng dựa trên ID của khách (guest_id trong DB)
	List<BookingJpaEntity> findByGuestId(Long guestId);
}