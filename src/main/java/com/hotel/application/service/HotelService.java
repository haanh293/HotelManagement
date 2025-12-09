package com.hotel.application.service;

import com.hotel.application.port.in.HotelUseCase;
import com.hotel.application.port.out.HotelRepositoryPort;
import com.hotel.domain.model.Hotel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService implements HotelUseCase {

    private final HotelRepositoryPort hotelPort;

    public HotelService(HotelRepositoryPort hotelPort) {
        this.hotelPort = hotelPort;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelPort.findAll();
    }

    @Override
    public List<Hotel> searchHotelsByCity(String city) {
        return hotelPort.findByCity(city);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelPort.findById(id).orElse(null);
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelPort.save(hotel);
    }
}