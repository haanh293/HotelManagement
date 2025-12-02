package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.InventoryUseCase;
import com.hotel.domain.model.Inventory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryUseCase inventoryUseCase;

    public InventoryController(InventoryUseCase inventoryUseCase) {
        this.inventoryUseCase = inventoryUseCase;
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryUseCase.createInventory(inventory));
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        return ResponseEntity.ok(inventoryUseCase.getAllInventories());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryUseCase.deleteInventory(id);
        return ResponseEntity.ok().build();
    }
}