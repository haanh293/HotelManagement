package com.hotel.application.port.in;

import com.hotel.domain.model.Invoice;
import com.hotel.domain.model.InvoiceStatus;
import java.util.List;

public interface InvoiceUseCase {
    Invoice createInvoice(Invoice invoice);
    List<Invoice> getAllInvoices();
    Invoice getInvoiceById(Long id);
    Invoice updateInvoiceStatus(Long id, InvoiceStatus status);
}