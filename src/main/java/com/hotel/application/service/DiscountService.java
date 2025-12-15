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
        // Mặc định số lượng là 1 nếu quên nhập
        if (discount.getQuantity() == null) {
            discount.setQuantity(1);
        }
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
        Discount discount = discountRepositoryPort.findValidDiscount(code, guestId)
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ hoặc đã hết hạn!"));
        
        // Kiểm tra số lượng còn không
        if (discount.getQuantity() <= 0) {
            throw new RuntimeException("Mã giảm giá đã hết lượt sử dụng!");
        }
        
        return discount;
    }
    @Override
    public Discount applyDiscount(String code, Long guestId) {
        // 1. Kiểm tra mã có hợp lệ không
        Discount discount = checkValidDiscount(code, guestId);

        // 2. Trừ số lượng đi 1
        discount.setQuantity(discount.getQuantity() - 1);
        
        // 3. Nếu số lượng về 0 -> Có thể đánh dấu isUsed = true (nếu muốn)
        if (discount.getQuantity() == 0) {
            discount.setIsUsed(true); 
        }

        // 4. Lưu cập nhật xuống Database
        return discountRepositoryPort.save(discount);
    }
}