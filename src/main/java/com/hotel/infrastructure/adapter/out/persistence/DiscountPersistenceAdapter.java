package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.DiscountRepositoryPort;
import com.hotel.domain.model.Discount;
import com.hotel.infrastructure.adapter.out.persistence.entity.DiscountJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataDiscountRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DiscountPersistenceAdapter implements DiscountRepositoryPort {

    private final SpringDataDiscountRepository repository;

    public DiscountPersistenceAdapter(SpringDataDiscountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Discount save(Discount discount) {
        DiscountJpaEntity entity = new DiscountJpaEntity();
        entity.setId(discount.getId());
        entity.setCode(discount.getCode());
        entity.setValue(discount.getValue());
        entity.setStartDate(discount.getStartDate());
        entity.setEndDate(discount.getEndDate());

        DiscountJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Discount> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Discount mapToDomain(DiscountJpaEntity entity) {
        return new Discount(entity.getId(), entity.getCode(), entity.getValue(), entity.getStartDate(), entity.getEndDate());
    }
}