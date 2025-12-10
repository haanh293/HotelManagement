package com.hotel.application.service;

import com.hotel.application.port.in.DiscountUseCase;
import com.hotel.application.port.out.DiscountRepositoryPort;
import com.hotel.domain.model.Discount;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscountService implements DiscountUseCase {

    private final DiscountRepositoryPort discountRepositoryPort;

    public DiscountService(DiscountRepositoryPort discountRepositoryPort) {
        this.discountRepositoryPort = discountRepositoryPort;
    }

    @Override
    public Discount createDiscount(Discount discount) {
        return discountRepositoryPort.save(discount);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepositoryPort.findAll();
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepositoryPort.deleteById(id);
    }

    // --- LOGIC MỚI BẠN CẦN ---
    @Override
    public Discount checkValidDiscount(String code, Long guestId) {
        // Gọi xuống Port để tìm mã phù hợp
        return discountRepositoryPort.findValidDiscount(code, guestId)
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ, đã hết hạn hoặc không dành cho bạn!"));
    }
}