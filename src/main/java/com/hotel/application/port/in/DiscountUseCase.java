package com.hotel.application.port.in;

import com.hotel.domain.model.Discount;
import java.util.List;

public interface DiscountUseCase {
    Discount createDiscount(Discount discount);
    List<Discount> getAllDiscounts();
    void deleteDiscount(Long id);
    Discount checkValidDiscount(String code, Long guestId);
    Discount applyDiscount(String code, Long guestId);
}