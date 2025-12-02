package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.HotelServiceRepositoryPort;
import com.hotel.domain.model.HotelService;
import com.hotel.infrastructure.adapter.out.persistence.entity.HotelServiceJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataHotelServiceRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelServicePersistenceAdapter implements HotelServiceRepositoryPort {

    private final SpringDataHotelServiceRepository repository;

    public HotelServicePersistenceAdapter(SpringDataHotelServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public HotelService save(HotelService service) {
        HotelServiceJpaEntity entity = new HotelServiceJpaEntity();
        entity.setId(service.getId());
        entity.setName(service.getName());
        entity.setPrice(service.getPrice());
        entity.setUnit(service.getUnit());
        entity.setStatus(service.getStatus());

        HotelServiceJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<HotelService> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private HotelService mapToDomain(HotelServiceJpaEntity entity) {
        return new HotelService(entity.getId(), entity.getName(), entity.getPrice(), entity.getUnit(), entity.getStatus());
    }
}