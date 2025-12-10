package com.hotel.application.port.out;

import com.hotel.domain.model.Discount;
import java.util.List;
import java.util.Optional;

public interface DiscountRepositoryPort {
    Discount save(Discount discount);
    List<Discount> findAll();
    void deleteById(Long id);
    Optional<Discount> findValidDiscount(String code, Long guestId);
}