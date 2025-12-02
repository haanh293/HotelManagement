package com.hotel.application.port.out;

import com.hotel.domain.model.HotelService;
import java.util.List;

public interface HotelServiceRepositoryPort {
    HotelService save(HotelService service);
    List<HotelService> findAll();
    void deleteById(Long id);
}