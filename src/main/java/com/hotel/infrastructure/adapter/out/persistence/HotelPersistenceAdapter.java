package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.HotelRepositoryPort;
import com.hotel.domain.model.Hotel;
import com.hotel.infrastructure.adapter.out.persistence.entity.HotelJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataHotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HotelPersistenceAdapter implements HotelRepositoryPort {

    private final SpringDataHotelRepository hotelRepo;

    public HotelPersistenceAdapter(SpringDataHotelRepository hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public List<Hotel> findAll() {
        return hotelRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Hotel> findByCity(String city) {
        return hotelRepo.findByCity(city).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Hotel> findById(Long id) {
        return hotelRepo.findById(id).map(this::toDomain);
    }

    @Override
    public Hotel save(Hotel hotel) {
        HotelJpaEntity entity = new HotelJpaEntity();
        
        entity.setId(hotel.getId());
        entity.setName(hotel.getName());
        entity.setCity(hotel.getCity());
        entity.setAddress(hotel.getAddress());
        entity.setDescription(hotel.getDescription());
        
        entity.setBasicPrice(hotel.getBasicPrice());

        return toDomain(hotelRepo.save(entity));
    }

    // Hàm chuyển đổi từ Entity sang Domain
    private Hotel toDomain(HotelJpaEntity e) {
        return new Hotel(
            e.getId(), 
            e.getName(), 
            e.getCity(), 
            e.getAddress(), 
            e.getDescription(),
            e.getBasicPrice()
        );
    }
}