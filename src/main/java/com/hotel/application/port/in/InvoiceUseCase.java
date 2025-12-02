package com.hotel.application.port.in;

import com.hotel.domain.model.Invoice;
import java.util.List;

public interface InvoiceUseCase {
    Invoice createInvoice(Invoice invoice);
    List<Invoice> getAllInvoices();
}