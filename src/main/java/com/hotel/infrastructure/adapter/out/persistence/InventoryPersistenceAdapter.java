package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.InventoryRepositoryPort;
import com.hotel.domain.model.Inventory;
import com.hotel.infrastructure.adapter.out.persistence.entity.InventoryJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataInventoryRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryPersistenceAdapter implements InventoryRepositoryPort {

    private final SpringDataInventoryRepository repository;

    public InventoryPersistenceAdapter(SpringDataInventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryJpaEntity entity = new InventoryJpaEntity();
        entity.setId(inventory.getId());
        entity.setName(inventory.getName());
        entity.setType(inventory.getType());
        entity.setStock(inventory.getStock());
        entity.setLocation(inventory.getLocation());

        InventoryJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Inventory> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Inventory mapToDomain(InventoryJpaEntity entity) {
        return new Inventory(entity.getId(), entity.getName(), entity.getType(), entity.getStock(), entity.getLocation());
    }
}