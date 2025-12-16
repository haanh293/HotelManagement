package com.hotel.application.service;

import com.hotel.application.port.in.InvoiceUseCase;
import com.hotel.application.port.out.InvoiceRepositoryPort;
import com.hotel.domain.model.Invoice;
import com.hotel.domain.model.InvoiceStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService implements InvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepositoryPort;

    public InvoiceService(InvoiceRepositoryPort invoiceRepositoryPort) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        if (invoice.getPaymentDate() == null) {
            invoice.setPaymentDate(LocalDate.now());
        }
        // Mặc định trạng thái là UNPAID nếu chưa có 
        if (invoice.getStatus() == null) {
            invoice.setStatus(InvoiceStatus.UNPAID);
        }
        return invoiceRepositoryPort.save(invoice);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepositoryPort.findById(id).orElse(null);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepositoryPort.findAll();
    }
}