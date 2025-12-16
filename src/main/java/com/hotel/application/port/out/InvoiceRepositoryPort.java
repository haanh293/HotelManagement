package com.hotel.application.port.out;

import com.hotel.domain.model.Invoice;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepositoryPort {
    Invoice save(Invoice invoice);
    List<Invoice> findAll();
    void deleteById(Long id);
    Optional<Invoice> findById(Long id);
    Optional<Invoice> findByBookingId(Long bookingId);
}