package com.hotel.application.port.in;

import com.hotel.domain.model.HotelService;
import java.util.List;

public interface HotelServiceUseCase {
    HotelService createService(HotelService service);
    List<HotelService> getAllServices();
    void deleteService(Long id);
}