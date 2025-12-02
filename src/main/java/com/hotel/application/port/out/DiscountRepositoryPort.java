package com.hotel.application.port.out;

import com.hotel.domain.model.Discount;
import java.util.List;

public interface DiscountRepositoryPort {
    Discount save(Discount discount);
    List<Discount> findAll();
    void deleteById(Long id);
}