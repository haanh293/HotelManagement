package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.DiscountRepositoryPort;
import com.hotel.domain.model.Discount;
import com.hotel.infrastructure.adapter.out.persistence.entity.DiscountJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataDiscountRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DiscountPersistenceAdapter implements DiscountRepositoryPort {

    private final SpringDataDiscountRepository repository;

    public DiscountPersistenceAdapter(SpringDataDiscountRepository repository) {
        this.repository = repository;
    }

    // 1. LƯU (SAVE) 
    @Override
    public Discount save(Discount discount) {
        DiscountJpaEntity entity = new DiscountJpaEntity();
        
        
        entity.setId(discount.getId());
        entity.setCode(discount.getCode());
        entity.setValue(discount.getValue());
        entity.setStartDate(discount.getStartDate());
        entity.setEndDate(discount.getEndDate());

        
        entity.setGuestId(discount.getGuestId());
        entity.setIsUsed(discount.getIsUsed());
        
        entity.setQuantity(discount.getQuantity());
        entity.setDescription(discount.getDescription());
        
        DiscountJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    // 2. TÌM TẤT CẢ 
    @Override
    public List<Discount> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    // 3. XÓA 
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Hàm này sẽ gọi xuống Repository để chạy câu Query kiểm tra GuestID và Date
    @Override
    public Optional<Discount> findValidDiscount(String code, Long guestId) {
        // Truyền ngày hiện tại (LocalDate.now()) vào để so sánh ngày hết hạn
        return repository.findValidDiscount(code, guestId, LocalDate.now())
                .map(this::mapToDomain);
    }

    // 5. CHUYỂN ĐỔI ENTITY -> DOMAIN
    private Discount mapToDomain(DiscountJpaEntity entity) {
        return new Discount(
                entity.getId(),
                entity.getCode(),
                entity.getValue(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getGuestId(),
                entity.getIsUsed(),
                
                entity.getQuantity(),
                entity.getDescription()
        );
    }
}