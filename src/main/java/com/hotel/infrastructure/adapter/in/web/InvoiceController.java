package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.InvoiceUseCase;
import com.hotel.domain.model.Invoice;
import com.hotel.domain.model.InvoiceStatus; // Import Enum
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

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceUseCase.getInvoiceById(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- API Mới: Cập nhật trạng thái ---
    // URL: PATCH /api/invoices/1/status?status=PAID
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id, 
            @RequestParam InvoiceStatus status) {
        try {
            Invoice updatedInvoice = invoiceUseCase.updateInvoiceStatus(id, status);
            return ResponseEntity.ok(updatedInvoice);
        } catch (RuntimeException e) {
            // Trả về lỗi 404 hoặc 400 nếu không tìm thấy hóa đơn
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}