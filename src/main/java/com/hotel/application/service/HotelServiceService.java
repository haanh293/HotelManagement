package com.hotel.application.service;

import com.hotel.application.port.in.HotelServiceUseCase;
import com.hotel.application.port.out.HotelServiceRepositoryPort;
import com.hotel.domain.model.HotelService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelServiceService implements HotelServiceUseCase {

    private final HotelServiceRepositoryPort repositoryPort;

    public HotelServiceService(HotelServiceRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HotelService createService(HotelService service) {
        return repositoryPort.save(service);
    }

    @Override
    public List<HotelService> getAllServices() {
        return repositoryPort.findAll();
    }

    @Override
    public void deleteService(Long id) {
        repositoryPort.deleteById(id);
    }
}