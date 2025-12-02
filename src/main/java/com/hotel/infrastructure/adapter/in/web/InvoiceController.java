package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.InvoiceUseCase;
import com.hotel.domain.model.Invoice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    public InvoiceController(InvoiceUseCase invoiceUseCase) {
        this.invoiceUseCase = invoiceUseCase;
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceUseCase.createInvoice(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceUseCase.getAllInvoices());
    }
}