package com.hotel.application.service;

import com.hotel.application.port.in.InventoryUseCase;
import com.hotel.application.port.out.InventoryRepositoryPort;
import com.hotel.domain.model.Inventory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService implements InventoryUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public InventoryService(InventoryRepositoryPort inventoryRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepositoryPort.save(inventory);
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepositoryPort.findAll();
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepositoryPort.deleteById(id);
    }
}