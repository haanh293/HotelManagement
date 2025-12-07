package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
@Repository
public interface SpringDataBookingRepository extends JpaRepository<BookingJpaEntity, Long> {
	// Tìm các đơn đặt phòng dựa trên ID của khách (guest_id trong DB)
	List<BookingJpaEntity> findByGuestId(Long guestId);
	@Query("SELECT b FROM BookingJpaEntity b WHERE b.room.id = :roomId " +
	           "AND b.status <> 'CANCELLED' " +
	           "AND ((b.checkInDate < :checkOutDate) AND (b.checkOutDate > :checkInDate))")
	    List<BookingJpaEntity> findOverlappingBookings(@Param("roomId") Long roomId, 
	                                                   @Param("checkInDate") LocalDate checkIn, 
	                                                   @Param("checkOutDate") LocalDate checkOut);
}