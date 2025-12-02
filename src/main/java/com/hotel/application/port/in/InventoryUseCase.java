package com.hotel.application.port.in;

import com.hotel.domain.model.Inventory;
import java.util.List;

public interface InventoryUseCase {
    Inventory createInventory(Inventory inventory);
    List<Inventory> getAllInventories();
    void deleteInventory(Long id);
}