package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.GuestRepositoryPort;
import com.hotel.domain.model.Guest;
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuestPersistenceAdapter implements GuestRepositoryPort {

    private final SpringDataGuestRepository springDataGuestRepository;

    public GuestPersistenceAdapter(SpringDataGuestRepository springDataGuestRepository) {
        this.springDataGuestRepository = springDataGuestRepository;
    }

    @Override
    public Guest save(Guest guest) {
        GuestJpaEntity entity = new GuestJpaEntity();
        entity.setId(guest.getId());
        entity.setFullName(guest.getFullName());
        entity.setPhoneNumber(guest.getPhoneNumber());
        entity.setEmail(guest.getEmail());
        entity.setAddress(guest.getAddress());
        
        
        entity.setUserId(guest.getUserId()); 
        

        GuestJpaEntity saved = springDataGuestRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Guest> findAll() {
        return springDataGuestRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    
    @Override
    public Guest getGuestByUserId(Long userId) {
        return springDataGuestRepository.findByUserId(userId)
                .map(this::mapToDomain)
                .orElse(null);
    }
    

    private Guest mapToDomain(GuestJpaEntity entity) {
        Guest guest = new Guest();
        guest.setId(entity.getId());
        guest.setFullName(entity.getFullName());
        guest.setPhoneNumber(entity.getPhoneNumber());
        guest.setEmail(entity.getEmail());
        guest.setAddress(entity.getAddress());
        guest.setUserId(entity.getUserId()); // Map ngược lại
        return guest;
    }
}