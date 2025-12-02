package com.hotel.application.port.out;

import com.hotel.domain.model.Inventory;
import java.util.List;

public interface InventoryRepositoryPort {
    Inventory save(Inventory inventory);
    List<Inventory> findAll();
    void deleteById(Long id);
}