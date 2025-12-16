package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataBookingRepository extends JpaRepository<BookingJpaEntity, Long> {

    @Query("SELECT b FROM BookingJpaEntity b WHERE b.roomId = :roomId " +
           "AND b.status <> 'CANCELLED' " +
           "AND ((b.checkInDate < :checkOutDate) AND (b.checkOutDate > :checkInDate))")
    List<BookingJpaEntity> findOverlappingBookings(@Param("roomId") Long roomId, 
                                                   @Param("checkInDate") LocalDate checkIn, 
                                                   @Param("checkOutDate") LocalDate checkOut);

    List<BookingJpaEntity> findByGuestId(Long guestId);
    Optional<BookingJpaEntity> findByBookingCode(String bookingCode);
    
    @Query("SELECT b FROM BookingJpaEntity b WHERE b.roomId = :roomId AND b.status = 'CHECKED_IN'")
    Optional<BookingJpaEntity> findActiveBookingByRoom(@Param("roomId") Long roomId);
    
    @Query("SELECT SUM(b.totalAmount) FROM BookingJpaEntity b " +
            "WHERE b.guestId = :guestId AND b.status IN ('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'COMPLETED')")
     BigDecimal calculateTotalSpending(@Param("guestId") Long guestId);
}