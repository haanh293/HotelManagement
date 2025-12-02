package com.hotel.application.port.out;

import com.hotel.domain.model.Invoice;
import java.util.List;

public interface InvoiceRepositoryPort {
    Invoice save(Invoice invoice);
    List<Invoice> findAll();
    void deleteById(Long id);
}