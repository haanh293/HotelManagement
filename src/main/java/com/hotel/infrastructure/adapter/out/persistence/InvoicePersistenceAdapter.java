package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.InvoiceRepositoryPort;
import com.hotel.domain.model.Invoice;
import com.hotel.domain.model.InvoiceStatus; // 1. Nhớ import Enum này
import com.hotel.infrastructure.adapter.out.persistence.entity.InvoiceJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataInvoiceRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InvoicePersistenceAdapter implements InvoiceRepositoryPort {

    private final SpringDataInvoiceRepository repository;

    public InvoicePersistenceAdapter(SpringDataInvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceJpaEntity entity = new InvoiceJpaEntity();
        entity.setId(invoice.getId());
        entity.setBookingId(invoice.getBookingId());
        entity.setTotalAmount(invoice.getTotalAmount());
        entity.setPaymentDate(invoice.getPaymentDate());
        entity.setPaymentMethod(invoice.getPaymentMethod());
        
        // 2. Thêm dòng này: Map status từ Domain -> Entity để lưu xuống DB
        entity.setStatus(invoice.getStatus()); 

        InvoiceJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Invoice> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    @Override
    public Optional<Invoice> findByBookingId(Long bookingId) {
        return repository.findByBookingId(bookingId)
                .map(this::mapToDomain);
    }

    // 3. Cập nhật hàm này: Map status từ Entity -> Domain
    private Invoice mapToDomain(InvoiceJpaEntity entity) {
        return new Invoice(
            entity.getId(), 
            entity.getBookingId(), 
            entity.getTotalAmount(), 
            entity.getPaymentDate(), 
            entity.getPaymentMethod(),
            entity.getStatus() // Thêm tham số status vào cuối
        );
    }
}