package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.HotelServiceUseCase;
import com.hotel.domain.model.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class HotelServiceController {

    private final HotelServiceUseCase serviceUseCase;

    public HotelServiceController(HotelServiceUseCase serviceUseCase) {
        this.serviceUseCase = serviceUseCase;
    }

    @PostMapping
    public ResponseEntity<HotelService> createService(@RequestBody HotelService service) {
        return ResponseEntity.ok(serviceUseCase.createService(service));
    }

    @GetMapping
    public ResponseEntity<List<HotelService>> getAllServices() {
        return ResponseEntity.ok(serviceUseCase.getAllServices());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceUseCase.deleteService(id);
        return ResponseEntity.ok().build();
    }
}