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

    // --- Logic mới thêm vào ---
    @Override
    public Invoice updateInvoiceStatus(Long id, InvoiceStatus status) {
        // 1. Tìm hóa đơn hiện tại
        Invoice existingInvoice = invoiceRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        // 2. Cập nhật trạng thái mới
        existingInvoice.setStatus(status);
        
        // 3. Lưu lại xuống DB (Adapter sẽ xử lý việc update dựa trên ID)
        return invoiceRepositoryPort.save(existingInvoice);
    }
    @Override
    public void cancelInvoiceByBookingId(Long bookingId) {
        // 1. Tìm hóa đơn dựa trên Booking ID
        Invoice invoice = invoiceRepositoryPort.findByBookingId(bookingId)
                .orElse(null); // Nếu không có thì thôi, không báo lỗi

        // 2. Nếu tìm thấy thì đổi trạng thái sang CANCELLED
        if (invoice != null) {
            // Chỉ hủy nếu hóa đơn chưa thanh toán hoặc đang chờ
            invoiceRepositoryPort.save(invoice);
        }
    }
}