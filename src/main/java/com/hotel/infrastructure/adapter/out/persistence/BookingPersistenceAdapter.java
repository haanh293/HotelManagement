package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.BookingRepositoryPort;

import com.hotel.domain.model.Booking;
import com.hotel.infrastructure.adapter.out.persistence.entity.BookingJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.RoomJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataRoomRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.time.LocalDate;
@Component
public class BookingPersistenceAdapter implements BookingRepositoryPort {

    private final SpringDataBookingRepository bookingRepo;
    private final SpringDataGuestRepository guestRepo;
    private final SpringDataRoomRepository roomRepo;

    public BookingPersistenceAdapter(SpringDataBookingRepository bookingRepo, 
                                     SpringDataGuestRepository guestRepo,
                                     SpringDataRoomRepository roomRepo) {
        this.bookingRepo = bookingRepo;
        this.guestRepo = guestRepo;
        this.roomRepo = roomRepo;
    }

    @Override
    public Booking save(Booking booking) {
        BookingJpaEntity entity = new BookingJpaEntity();
        
        // 1. Tìm Guest và Room từ CSDL dựa trên ID
        GuestJpaEntity guest = guestRepo.findById(booking.getGuestId()).orElse(null);
        RoomJpaEntity room = roomRepo.findById(booking.getRoomId()).orElse(null);

        // 2. Gán vào Booking Entity
        entity.setGuest(guest);
        entity.setRoom(room);
        entity.setCheckInDate(booking.getCheckInDate());
        entity.setCheckOutDate(booking.getCheckOutDate());
        entity.setTotalAmount(booking.getTotalAmount());
        entity.setStatus(booking.getStatus());

        // 3. Lưu
        BookingJpaEntity saved = bookingRepo.save(entity);
        
        // 4. Trả về Domain
        return mapToDomain(saved);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepo.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Booking mapToDomain(BookingJpaEntity entity) {
        Long guestId = (entity.getGuest() != null) ? entity.getGuest().getId() : null;
        Long roomId = (entity.getRoom() != null) ? entity.getRoom().getId() : null;
        
        return new Booking(entity.getId(), guestId, roomId, 
                           entity.getCheckInDate(), entity.getCheckOutDate(), 
                           entity.getTotalAmount(), entity.getStatus());
    }
    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepo.findById(id).map(this::mapToDomain);
    }
    @Override
    public List<Booking> findByGuestId(Long guestId) {
        return bookingRepo.findByGuestId(guestId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }
 // Triển khai hàm kiểm tra
    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<BookingJpaEntity> overlaps = bookingRepo.findOverlappingBookings(roomId, checkIn, checkOut);
        return overlaps.isEmpty(); // Nếu danh sách rỗng -> Phòng trống -> True
    }
}