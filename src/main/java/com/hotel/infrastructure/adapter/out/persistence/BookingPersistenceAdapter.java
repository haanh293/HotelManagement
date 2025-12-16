package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.BookingRepositoryPort;
import com.hotel.domain.model.Booking;
import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookingPersistenceAdapter implements BookingRepositoryPort {

    private final SpringDataBookingRepository bookingRepo;

    public BookingPersistenceAdapter(SpringDataBookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Booking save(Booking booking) {
        BookingJpaEntity entity = new BookingJpaEntity();
        
        // Map dữ liệu
        entity.setId(booking.getId());
        entity.setGuestId(booking.getGuestId());
        entity.setRoomId(booking.getRoomId());
        entity.setHotelId(booking.getHotelId());
        entity.setCheckInDate(booking.getCheckInDate());
        entity.setCheckOutDate(booking.getCheckOutDate());
        entity.setTotalAmount(booking.getTotalAmount());
        entity.setStatus(booking.getStatus());

        entity.setAdults(booking.getAdults());
        entity.setChildrenUnder3(booking.getChildrenUnder3());
        entity.setChildren3To5(booking.getChildren3To5());
        entity.setChildren6To12(booking.getChildren6To12());
        
        entity.setRoomType(booking.getRoomType());
        entity.setViewType(booking.getViewType());
        entity.setPosition(booking.getPosition());
        entity.setLightType(booking.getLightType());
        
        entity.setBookingCode(booking.getBookingCode());

        BookingJpaEntity saved = bookingRepo.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepo.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByGuestId(Long guestId) {
        return bookingRepo.findByGuestId(guestId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepo.findById(id).map(this::mapToDomain);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, java.time.LocalDate checkIn, java.time.LocalDate checkOut) {
       // Gọi Repository để kiểm tra trùng lịch
       return bookingRepo.findOverlappingBookings(roomId, checkIn, checkOut).isEmpty();
    }
    // ------------------------------------------------------------
    
    public void deleteById(Long id) {
        bookingRepo.deleteById(id);
    }
    @Override
    public Optional<Booking> findByBookingCode(String bookingCode) {
        return bookingRepo.findByBookingCode(bookingCode)
                .map(this::mapToDomain);
    }

    // Hàm chuyển đổi
    private Booking mapToDomain(BookingJpaEntity entity) {
        return new Booking(
                entity.getId(),
                entity.getGuestId(),
                entity.getRoomId(),
                entity.getHotelId(),
                entity.getCheckInDate(),
                entity.getCheckOutDate(),
                entity.getTotalAmount(),
                entity.getStatus(),
                entity.getAdults(),
                entity.getChildrenUnder3(),
                entity.getChildren3To5(),
                entity.getChildren6To12(),
                entity.getRoomType(),
                entity.getViewType(),
                entity.getPosition(),
                entity.getLightType(),
                entity.getBookingCode()
        );
    }
}