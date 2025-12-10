package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.GuestRepositoryPort;
import com.hotel.domain.model.Guest;
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataBookingRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuestPersistenceAdapter implements GuestRepositoryPort {

    private final SpringDataGuestRepository springDataGuestRepository;
    private final SpringDataBookingRepository bookingRepo; // <--- 1. KHAI BÁO THÊM

    // Cập nhật Constructor để tiêm BookingRepo vào
    public GuestPersistenceAdapter(SpringDataGuestRepository springDataGuestRepository,
                                   SpringDataBookingRepository bookingRepo) {
        this.springDataGuestRepository = springDataGuestRepository;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Guest save(Guest guest) {
        GuestJpaEntity entity = new GuestJpaEntity();
        
        // Map các thông tin cơ bản cũ
        entity.setId(guest.getId());
        entity.setFullName(guest.getFullName());
        entity.setPhoneNumber(guest.getPhoneNumber());
        entity.setEmail(guest.getEmail());
        entity.setAddress(guest.getAddress());
        entity.setUserId(guest.getUserId());
        
        // --- 2. CẬP NHẬT TRƯỜNG MỚI ĐỂ LƯU ---
        entity.setDateOfBirth(guest.getDateOfBirth());
        entity.setGender(guest.getGender());
        // -------------------------------------

        GuestJpaEntity saved = springDataGuestRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Guest> findAll() {
        return springDataGuestRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Guest getGuestByUserId(Long userId) {
        return springDataGuestRepository.findByUserId(userId)
                .map(this::mapToDomain)
                .orElse(null);
    }

    // --- 3. LOGIC CHUYỂN ĐỔI + TÍNH TIỀN ---
    private Guest mapToDomain(GuestJpaEntity entity) {
        Guest guest = new Guest();
        
        guest.setId(entity.getId());
        guest.setFullName(entity.getFullName());
        guest.setPhoneNumber(entity.getPhoneNumber());
        guest.setEmail(entity.getEmail());
        guest.setAddress(entity.getAddress());
        guest.setUserId(entity.getUserId());
        
        // Map các trường mới
        guest.setDateOfBirth(entity.getDateOfBirth());
        guest.setGender(entity.getGender());

        // --- TÍNH TỔNG TIỀN ĐÃ CHI ---
        // Gọi Repository để tính tổng các đơn thành công
        BigDecimal totalSpent = bookingRepo.calculateTotalSpending(entity.getId());
        
        // Nếu chưa đặt đơn nào thì trả về 0 thay vì null
        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }
        
        guest.setTotalSpending(totalSpent); 
        // -----------------------------

        return guest;
    }
    @Override
    public java.util.Optional<Guest> findById(Long id) {
        return springDataGuestRepository.findById(id)
                .map(this::mapToDomain); // Tái sử dụng hàm mapToDomain xịn (có tính tiền)
    }
}